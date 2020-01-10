package cn.edu.fudan.mtsc.query;

import cn.edu.fudan.mtsc.common.Metadata;
import cn.edu.fudan.mtsc.common.MetadataGet;
import cn.edu.fudan.mtsc.group.GroupResultItem;
import cn.edu.fudan.mtsc.query.common.JaccardDistance;
import cn.edu.fudan.mtsc.result.GroupInfo;
import cn.edu.fudan.mtsc.result.SegmentInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sherry on 19-12-25.
 */
public class Evolution {

    public static void main(String[] args) {
        String filename = "Rare-event"; //OppAnomaly, Rare-event, PamapSelect, Lying, Running, Sitting, Standing, Walking, Cycling, Accending
        // 1.get metadata of all datasets
        List<Metadata> metadataList = MetadataGet.getInputDataMetadata("data/info.txt");
        for (Metadata metadata : metadataList) {
            if (metadata.getDataSet().equals(filename)) {
                int size = metadata.getSize();
                // get group information
                QuerySegment query = new QuerySegment(metadata.getCompressedFilePath());
                List<SegmentInfo> segmentInfo = query.getSegmentInfo();
                System.out.println("Total windows are " + segmentInfo.size());
                for (SegmentInfo si : segmentInfo) {
                    outputGroupInfo(size, si.getGroupInfos());
                }
//                for (SegmentInfo si : segmentInfo) {
//                    System.out.println(si.getGroupInfos().size()+ " " + si.getGroupInfos().get(0).getBaseBuckets().size() + " " + si.getMultiSingleBuckets().size());
//                }
//                for (int i = 0; i < segmentInfo.size(); i++) {
//                    for (int j = 0; j < segmentInfo.size(); j++) {
//                        //System.out.println("i=" + i + ", j=" + j);
//                        System.out.format("%.3f", getSimilarity(size, segmentInfo.get(i).getGroupInfos(), segmentInfo.get(j).getGroupInfos()));
//                        System.out.print(" ");
//                    }
//                    System.out.println();
//                }
                break;
            }
        }
    }

    public static void test() {
        List<GroupInfo> w1 = new ArrayList<>();
        List<GroupInfo> w2 = new ArrayList<>();
        w1.add(new GroupInfo(new GroupResultItem(1, Arrays.asList(1,2,3)), null, null));
        w1.add(new GroupInfo(new GroupResultItem(2, Arrays.asList(4,6)), null, null));
        w1.add(new GroupInfo(new GroupResultItem(3, Arrays.asList(5,7)), null, null));
        w2.add(new GroupInfo(new GroupResultItem(1, Arrays.asList(1,2)), null, null));
        w2.add(new GroupInfo(new GroupResultItem(2, Arrays.asList(3,5,7)), null, null));
        w2.add(new GroupInfo(new GroupResultItem(3, Arrays.asList(4,6)), null, null));
        System.out.println(getSimilarity(7, w1, w2));
    }

    static void outputGroupInfo(int size, List<GroupInfo> w) {
        List<Integer> single = getSingleId(w, size);
        outputSegmentInfo(w, single);
    }


    // 输入两个窗口的分组情况，返回相似度
    static double getSimilarity(int size, List<GroupInfo> w1, List<GroupInfo> w2) {
        int n = w1.size(), m = w2.size();
        List<Integer> single1 = getSingleId(w1, size);
        List<Integer> single2 = getSingleId(w2, size);
        n += single1.size();
        m += single2.size();

        double[][] dis = new double[n][m];
        List<SimandPos> sortedPos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> seg1 = new ArrayList<>();
            if (i < w1.size()) {
                seg1 = w1.get(i).getGroupResultItem().getIds();
            } else {
                seg1.add(single1.get(i - w1.size()));
            }
            for (int j = 0; j < m; j++) {
                List<Integer> seg2 = new ArrayList<>();
                if (j < w2.size()) {
                    seg2 = w2.get(j).getGroupResultItem().getIds();
                } else {
                    seg2.add(single2.get(j - w2.size()));
                }
                dis[i][j] = JaccardDistance.compute(seg1, seg2);
                sortedPos.add(new SimandPos(dis[i][j], i, j));
            }
        }
        sortedPos.sort(Comparator.comparingDouble(SimandPos::getSimilarity).reversed());
        boolean[] vis1 = new boolean[n], vis2 = new boolean[m];
        double maxSim = 0;
        for (SimandPos sp : sortedPos) {
            if (vis1[sp.x] || vis2[sp.y]) continue;
            maxSim += sp.similarity;
            vis1[sp.x] = vis2[sp.y] = true;
        }
        double res = maxSim * 2 / (n + m);
//        System.out.print("{");
//        System.out.print("{");
//        outputSegmentInfo(w1, single1);
//        System.out.println("}");
//        System.out.print("; ");
//        System.out.print("{");
//        outputSegmentInfo(w2, single2);
//        System.out.println("}}");
//        System.out.println("similarity = " + res);
//        System.out.println(res);
        return res;
    }

    private static List<Integer> getSingleId(List<GroupInfo> w, int size) {
        boolean[] vis = new boolean[size];
        for (GroupInfo gi : w) {
            for (int x : gi.getGroupResultItem().getIds()) {
                vis[x] = true;
            }
        }
        List<Integer> single = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (!vis[i]) single.add(i);
        }
        return single;
    }

    private static void outputSegmentInfo(List<GroupInfo> w, List<Integer> single) {
        for (GroupInfo gi : w) {
            List<Integer> sorted = gi.getGroupResultItem().getIds();
            sorted.sort(Comparator.comparingInt(o -> o));
            System.out.print(sorted);
        }
        for (int x : single) {
            System.out.print("[" + x + "]");
        }
        System.out.println();
    }

    private static class SimandPos {
        double similarity;
        int x, y;
        SimandPos(double similarity, int x, int y) {
            this.similarity = similarity;
            this.x = x;
            this.y = y;
        }

        public double getSimilarity() {
            return similarity;
        }
    }

}
