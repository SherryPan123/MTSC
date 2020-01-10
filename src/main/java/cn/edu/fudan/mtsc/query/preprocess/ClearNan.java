package cn.edu.fudan.mtsc.query.preprocess;

import java.io.*;

/**
 * Created by sherry on 20-1-4.
 */
public class ClearNan {

    public static void main(String[] args) {
        String inputFileName = "data/pamap_preprocess/walkingN.csv";
        String outputFileName = "data/pamap/walking.csv";
        int cntLine = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            FileWriter fw = new FileWriter(outputFileName);
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                boolean isLegal = true;
                if (data.length != 51) isLegal = false;
                for (String d : data) {
                    if (d.equals("NaN")) {
                        isLegal = false;
                        break;
                    }
                }
                if (isLegal) {
                    fw.append(line);
                    fw.append("\n");
                    cntLine++;
                }
            }
            fw.flush();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("file name: " + outputFileName);
        System.out.println("size = 51");
        System.out.println("length = " + cntLine);
    }
}
