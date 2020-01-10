package cn.edu.fudan.mtsc.query;

import cn.edu.fudan.mtsc.common.Metadata;
import cn.edu.fudan.mtsc.common.MetadataGet;
import cn.edu.fudan.mtsc.query.common.JaccardDistance;
import cn.edu.fudan.mtsc.result.GroupInfo;
import cn.edu.fudan.mtsc.result.SegmentInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by sherry on 19-12-25.
 */
public class Abnormal {

    public static void main(String[] args) {
//        String datasetName = "Rare-event";
//        int size = 52, winSize = 368;
//        String filename = "exp/abnormal/max_percent_jac_dis.csv";
        String datasetName = "OppAnomaly";
        int size = 15, winSize = 447;
        String filename = "exp/abnormal/opp_max_percent_jac_dis.csv";
//        preprocess(size, winSize, datasetName, filename);
        double[][] maxDis = readFromFile(size, winSize, filename);
        for (int i = 0; i < winSize; i++) {
            System.out.println(checkWindow(i, size, maxDis));
        }
    }

    private static double[][] readFromFile(int size, int winSize, String filename) {
        double[][] maxDis = new double[size][winSize];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int cntLine = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                for (int i = 0; i < size; i++) {
                    maxDis[i][cntLine] = Double.valueOf(data[i]);
                }
                cntLine++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxDis;
    }


    private static void outputToFile(int size, int winSize, double[][] maxDis, String outputFilename) {
        try {
            FileWriter fw = new FileWriter(outputFilename);
            for (int i = 0; i < winSize; i++) {
                for (int j = 0; j < size; j++) {
                    if (j > 0) fw.append(",");
                    fw.append(String.valueOf(maxDis[j][i]));
                }
                fw.append("\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void preprocess(int size, int winSize, String datasetName, String outputFilename) {
        double[][] maxDis = new double[size][winSize];
        for (int signalId = 0; signalId < size; signalId++) {
            maxDis[signalId] = getJacDis(signalId, datasetName);
        }
        outputToFile(size, winSize, maxDis, outputFilename);
    }

    private static int checkWindow(int winId, int size, double[][] maxDis) {
        double eps1 = 0.85;
        double eps2 = 0.5;
        int cnt = 0;
        for (int i = 0; i < size; i++) {
            if (maxDis[i][winId] < eps1) cnt++;
            if (cnt > eps2 * size) return 1;
        }
        return 0;
    }

    private static double[] getJacDis(int signalId, String filename) {
        // 1.get metadata of all datasets
        List<Metadata> metadataList = MetadataGet.getInputDataMetadata("data/info.txt");
        for (Metadata metadata : metadataList) {
            if (metadata.getDataSet().equals(filename)) {
                // get group information
                QuerySegment query = new QuerySegment(metadata.getCompressedFilePath());
                List<SegmentInfo> segmentInfo = query.getSegmentInfo();
                List<List<Integer>> idInWhichGroup = new ArrayList<>();
                for (SegmentInfo si : segmentInfo) {
                    List<Integer> group = getGroupById(si, signalId);
                    idInWhichGroup.add(group);
                }
                double[] maxDis = new double[idInWhichGroup.size()];
                double percent = 0.95;
                for (int i = 0; i < idInWhichGroup.size(); i++) {
                    maxDis[i] = getTopPercent(percent, idInWhichGroup, i);
                    //System.out.print(maxDis[i] + " ");
                }
                //System.out.println();
                return maxDis;
            }
        }
        return null;
    }

    private static double getTopPercent(double percent, List<List<Integer>> idInWhichGroup, int id) {
        List<Double> dis = new ArrayList<>();
        for (int j = 0; j < idInWhichGroup.size(); j++) {
            if (id == j) continue;
            dis.add(JaccardDistance.compute(idInWhichGroup.get(id), idInWhichGroup.get(j)));
        }
        Collections.sort(dis);
        int topK = (int)(percent * idInWhichGroup.size());
        if (topK >= dis.size()) return dis.get(dis.size() - 1);
        return dis.get(topK);
    }

    private static List<Integer> getGroupById(SegmentInfo si, int signalId) {
        List<GroupInfo> groups = si.getGroupInfos();
        for (GroupInfo gi : groups) {
            for (int i : gi.getGroupResultItem().getIds()) {
                if (i == signalId) {
                    return gi.getGroupResultItem().getIds();
                }
            }
        }
        return Arrays.asList(signalId);
    }
}
