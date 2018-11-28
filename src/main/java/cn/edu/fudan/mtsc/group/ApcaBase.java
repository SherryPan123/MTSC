package cn.edu.fudan.mtsc.group;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.APCA;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-4-4.
 */
public class ApcaBase {

    // input: val & group result
    private double[][] val;
    private int size;
    private int w;
    private double eps;

    // output: statistic
    private int sumBaseBucketNum;
    private int singleNum;
    private int sumSingleBucketNum;
    // output: compress result
    public List<GroupResultItem> groups; // groupNum:  number of groups
    public List<List<HorizontalBucket>> multiBaseBuckets;
    public List<Pair<Integer, List<HorizontalBucket>>> multiSingleBuckets;

    private List<Integer> singleIds;

    ApcaBase(double[][] val) {
        this.val = val;
        size = val.length;
        w = val[0].length;
        multiBaseBuckets = new ArrayList<>();
        multiSingleBuckets = new ArrayList<>();
    }

    void setEps(double eps) {
        this.eps = eps;
    }

    void setGroups(List<GroupResultItem> groups) {
        this.groups = groups;
    }

    void computeCompressRate(Statistic statistic) {
        int T = 0;
        for (GroupResultItem group : groups) {
            int number = group.getIds().size();
            T += number;
        }
        statistic.numerator = size * w;
        statistic.denominator = T + sumBaseBucketNum * 1.5 + sumSingleBucketNum * 1.5;
    }

    void compressBaseAndSingle() {
        compressGroupBase();
        compressSingle();
    }

    private void compressGroupBase() {
        sumBaseBucketNum = 0;
        for (GroupResultItem gri : groups) {
            List<Integer> group = gri.getIds();
            Pair<Double, Double>[] baseRange = new Pair[w];
            for (int j = 0; j < w; j++) {
                double mx = 0.0, mini = 0.0;
                for (int k = 0; k < group.size(); k++) {
                    int id = group.get(k);
                    if (k == 0 || mx < val[id][j]) {
                        mx = val[id][j];
                    }
                    if (k == 0 || mini > val[id][j]) {
                        mini = val[id][j];
                    }

                }
                baseRange[j] = new Pair<>(mx-eps, mini+eps);
            }
            List<HorizontalBucket> buckets = new ArrayList<>();
            Pair<Double, Double> curBaseRange = baseRange[0];
            int bucketCnt = 0;
            for (int j = 1; j < w; j++) {
                if (baseRange[j].getFirst() > curBaseRange.getSecond() || baseRange[j].getSecond() < curBaseRange.getFirst()) {
                    double avgValue = (curBaseRange.getFirst() + curBaseRange.getSecond()) / 2.0;
                    buckets.add(new HorizontalBucket(avgValue, j-1));
                    bucketCnt++;
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
            if (multiBaseBuckets == null) multiBaseBuckets = new ArrayList<>();
            multiBaseBuckets.add(buckets);
            bucketCnt++;
            sumBaseBucketNum += bucketCnt;
        }
    }

    private void compressSingle() {
        singleNum = 0;
        sumSingleBucketNum = 0;
        for (Integer i : this.getSingleIds()) {
            List<HorizontalBucket> buckets = APCA.runSinglePCA(val[i], eps);
            if (multiSingleBuckets == null) multiSingleBuckets = new ArrayList<>();
            multiSingleBuckets.add(new Pair<>(i, buckets));
            sumSingleBucketNum += buckets.size();
            singleNum++;
        }
    }

    private List<Integer> getSingleIds() {
        //if (singleIds != null) return singleIds;
        boolean[] inGroup = new boolean[size];
        for (GroupResultItem gri : groups) {
            List<Integer> group = gri.getIds();
            for (Integer id : group) {
                inGroup[id] = true;
            }
        }
        singleIds = new ArrayList<>();
        for (int i=0; i<size; i++) {
            if (inGroup[i]) continue;
            singleIds.add(i);
        }
        return singleIds;
    }

}
