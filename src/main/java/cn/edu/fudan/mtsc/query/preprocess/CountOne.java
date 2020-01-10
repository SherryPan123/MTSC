package cn.edu.fudan.mtsc.query.preprocess;

import java.io.*;

/**
 * Created by sherry on 20-1-6.
 */
public class CountOne {

    public static void main(String[] args) {
        int win = 50;
        combineWindow(win);
//        separateWindow(win);
    }

    private static void separateWindow(int win) {
        String inputFilename = "data/abnormal/label_win_52_90_98_50.csv";
        String outputFilename = "data/abnormal/label_timestamp_52_90_98_50.csv";
        try {
            FileWriter fw = new FileWriter(outputFilename);
            BufferedReader br = new BufferedReader(new FileReader(inputFilename));
            String line;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < win; i++) {
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

    private static void combineWindow(int win) {
        String filename = "data/abnormal/label.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int cntLine = 0;
            int oneCnt = 0;
            while ((line = br.readLine()) != null) {
                cntLine++;
                String[] data = line.split(",");
                if (data[0].equals("1")) oneCnt++;
                if (cntLine % win == 0) {
                    if (oneCnt >= 2) System.out.println(1);
                    else System.out.println(0);
                    oneCnt = 0;
                }
            }
            if (oneCnt >= 2) System.out.println(1);
            else System.out.println(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
