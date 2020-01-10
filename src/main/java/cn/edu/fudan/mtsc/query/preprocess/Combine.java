package cn.edu.fudan.mtsc.query.preprocess;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 20-1-4.
 */
public class Combine {

    public static void main(String[] args) {
        combineOpprentice();
    }

    private static void combineOpprentice() {
        String outputFileName = "data/opprentice/opp.csv";
        List<List<Double>> oppData = new ArrayList<>();
        int len = 1000000;
        File dir = new File("data/opprentice/raw");
        File[] files = dir.listFiles();
        try {
            for (File file : files) {
                System.out.println(file.getAbsoluteFile());
                List<Double> dataArray = new ArrayList<>();
                BufferedReader br = new BufferedReader(new FileReader(file));
                br.readLine(); // read title
                String line;
                while ((line = br.readLine()) != null) {
                    double val = Double.valueOf(line.split(",")[1]);
                    dataArray.add(val);
                }
                oppData.add(normalize(dataArray));
                len = Math.min(len, oppData.get(oppData.size() - 1).size());
            }
            FileWriter fw = new FileWriter(outputFileName);
            for (int j = 0; j < len; j++) {
                for (int i = 0; i < oppData.size(); i++) {
                    if (i > 0) fw.append(",");
                    fw.append(String.valueOf(oppData.get(i).get(j)));
                }
                fw.append("\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Double> normalize(List<Double> dataArray) {
        // get average and max value
        double avg = 0d, max = -Double.MAX_VALUE;
        for (int i = 0; i < dataArray.size(); i++) {
            double val = dataArray.get(i);
            avg = (avg * i + val) / (i + 1);
            max = Math.max(max, val);
        }
        List<Double> data = new ArrayList<>();
        for (double val : dataArray) {
            data.add(100.0 * (val - avg) / max); // to be [-100, 100]
        }
        return data;
    }

    private static void orderCombine() {
        String outputFileName = "data/pamap_select.csv";
        String[] files = {
                "data/pamap/select/walking.csv",
                "data/pamap/select/cycling.csv",
                "data/pamap/select/running.csv",
                "data/pamap/select/accending.csv",
                "data/pamap/select/lying.csv"
        };
        try {
            FileWriter fw = new FileWriter(outputFileName);
            for (String file : files) {
                System.out.println(file);
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    fw.append(line);
                    fw.append("\n");
                }
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void randomCombine() {
        String outputFileName = "data/pamap_select.csv";
        File dir = new File("data/pamap/select");
        File[] files = dir.listFiles();
        try {
            FileWriter fw = new FileWriter(outputFileName);
            for (File file : files) {
                System.out.println(file.getAbsoluteFile());
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    fw.append(line);
                    fw.append("\n");
                }
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
