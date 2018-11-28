package cn.edu.fudan.mtsc.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by sherry on 18-11-27.
 */
public class MetadataGet {

    public static List<Metadata> getInputDataMetadata(String filePath) {
        List<Metadata> metadataList = new ArrayList<>();
        File f = new File(filePath);
        if (!f.exists()) {
            System.err.println(filePath + " not found!");
            System.exit(0);
        }
        try (BufferedReader br = new BufferedReader(new FileReader("data/info.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(":");
                try {
                    Metadata metadata = new Metadata(data[0], data[1], data[2], data[3],
                            Integer.parseInt(data[4]), Integer.parseInt(data[5]), Float.parseFloat(data[6]));
                    metadataList.add(metadata);
                } catch (IllegalFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadataList;
    }
}
