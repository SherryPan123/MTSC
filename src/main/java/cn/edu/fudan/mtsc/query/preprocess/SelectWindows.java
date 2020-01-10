package cn.edu.fudan.mtsc.query.preprocess;

import java.io.*;

/**
 * Created by sherry on 20-1-4.
 */
public class SelectWindows {

    public static void main(String[] args) {
        String[] inputs = {
                "data/pamap/walking.csv",
                "data/pamap/cycling.csv",
                "data/pamap/running.csv",
                "data/pamap/accending.csv",
                "data/pamap/lying.csv"
        };
        String[] outputs = {
                "data/pamap/select/walking.csv",
                "data/pamap/select/cycling.csv",
                "data/pamap/select/running.csv",
                "data/pamap/select/accending.csv",
                "data/pamap/select/lying.csv"
        };
        int[][] actionWindows = {
                {3, 19, 21},
                {3, 12, 13},
                {2, 3, 9},
                {1, 3, 9},
                {4, 5, 8}
        };
        int win = 1000;
        for (int i = 0; i < inputs.length; i++) {
            selectWindow(inputs[i], outputs[i], actionWindows[i], win);
        }
    }

    private static void selectWindow(String inputFileName, String outputFileName, int[] windows, int win) {
        int id = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFileName))) {
            FileWriter fw = new FileWriter(outputFileName);
            String line;
            int cntLine = 1;
            while ((line = br.readLine()) != null) {
                if (cntLine == windows[id] * win + 1) {
                    int cnt = 0;
                    while (cnt < win) {
                        fw.append(line);
                        fw.append("\n");
                        cnt++;
                        if (cnt < win) line = br.readLine();
                    }
                    cntLine += win;
                    id++;
                    if (id >= windows.length) break;
                } else cntLine++;
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
        System.out.println("length = " + win * windows.length);
    }
}
