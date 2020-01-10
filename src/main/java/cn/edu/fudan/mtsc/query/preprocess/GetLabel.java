package cn.edu.fudan.mtsc.query.preprocess;

import java.io.*;

/**
 * Created by sherry on 20-1-10.
 */
public class GetLabel {
    public static void main(String[] args) {
        String outputFileName = "data/opprentice/opp_label.csv";
        int len = 44607;
        File dir = new File("data/opprentice/raw");
        File[] files = dir.listFiles();
        try {
            int[] label = new int[len];
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i].getAbsoluteFile());
                BufferedReader br = new BufferedReader(new FileReader(files[i]));
                br.readLine(); // read title
                String line;
                int cntLine = 0;
                while ((line = br.readLine()) != null) {
                    int val = Integer.valueOf(line.split(",")[2]);
                    if (val == 1 && cntLine < len) label[cntLine] = 1;
                    cntLine++;
                }
            }
            FileWriter fw = new FileWriter(outputFileName);
            for (int j = 0; j < len; j++) {
                fw.append(String.valueOf(label[j]));
                fw.append("\n");
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
