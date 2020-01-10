package cn.edu.fudan.mtsc.group;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.APCA;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 20-1-7.
 */
public class ApcaDegree {

    // input: val & group result
    private double[][] val;

    private int size;
    private int w;
    private double eps;

    // output: statistic
    private int sumBaseBucketNum;
    private int avgBaseBucketNum;
    private int singleNum;
    private int sumSingleBucketNum;
    private int avgSingleBucketNum;
    // output: compress result
    public List<GroupResultItem> groups; // groupNum:  number of groups
    public List<Pair<Pair<Integer, Double>, List<HorizontalBucket>>> multiBuckets;


    public ApcaDegree(double[][] val) {
        this.val = val;
        size = val.length;
        w = val[0].length;
        multiBuckets = new ArrayList<>();
    }

    public void setEps(double eps) {
        this.eps = eps;
    }

    public void setGroups(List<GroupResultItem> groups) {
        this.groups = groups;
    }

    public void compressBases() {
        sumBaseBucketNum = 0;
        sumSingleBucketNum = 0;
        List<Integer> singleIds = new ArrayList<>();
        for (int i=0; i<size; i++) {
            multiBuckets.add(new Pair<>(new Pair<>(0, 0d), new ArrayList<>()));
        }
        boolean[] inGroup = new boolean[size];
        for (GroupResultItem gri : groups) {
            List<Integer> group = gri.getIds();
            int baseId = group.get(0);
            // 得到base在w每个点的范围
            Pair<Double, Double>[] baseRange = new Pair[w];
            for (int j=0; j<w; j++) {
                double mx = 0.0, mini = 0.0;
                for (int k=0; k<group.size(); k++) {
                    int id = group.get(k);
                    inGroup[id] = true;
                    if (k == 0 || mx < val[id][j]) {
                        mx = val[id][j];
                    }
                    if (k == 0 || mini > val[id][j]) {
                        mini = val[id][j];
                    }

                }
                baseRange[j] = new Pair<>(mx-eps, mini+eps);
            }
            // 压缩base
            List<HorizontalBucket> buckets = new ArrayList<>();
            Pair<Double, Double> curBaseRange = baseRange[0];
            for (int j=1; j<w; j++) {
                if (baseRange[j].getFirst() > curBaseRange.getSecond() || baseRange[j].getSecond() < curBaseRange.getFirst()) {
                    double avgValue = (curBaseRange.getFirst() + curBaseRange.getSecond()) / 2.0;
                    buckets.add(new HorizontalBucket(avgValue, j-1));
                    curBaseRange = baseRange[j];
                } else {
                    double curFirst, curSecond;
                    curFirst = Math.max(baseRange[j].getFirst(), curBaseRange.getFirst());
                    curSecond = Math.min(baseRange[j].getSecond(), curBaseRange.getSecond());
                    curBaseRange = new Pair<>(curFirst, curSecond);
                }
            }
            double avgValue = (curBaseRange.getFirst() + curBaseRange.getSecond()) / 2.0;
            buckets.add(new HorizontalBucket(avgValue, w-1));
            sumBaseBucketNum += buckets.size();
            multiBuckets.set(baseId, new Pair<>(new Pair<>(baseId, 0.0), buckets));
            for (int i=1; i<group.size(); i++) {
                int id = group.get(i);
                multiBuckets.set(id, new Pair<>(new Pair<>(baseId, 0.0), null));
            }
        }

        for (int i=0; i<size; i++) {
            if (inGroup[i]) continue;
            singleIds.add(i);
            List<HorizontalBucket> buckets = APCA.runSinglePCA(val[i], eps);
            sumSingleBucketNum += buckets.size();
            multiBuckets.set(i, new Pair<>(new Pair<>(i, 0.0), buckets));
        }
        singleNum = singleIds.size();
        if (singleNum == 0) avgSingleBucketNum = -1;
        else avgSingleBucketNum = sumSingleBucketNum / singleNum;

    }

    public void computeCompressRate(Statistic statistic) {
        statistic.numerator = size*w*1;
        statistic.denominator = sumBaseBucketNum*1.5 + sumSingleBucketNum*1.5;
    }
}
