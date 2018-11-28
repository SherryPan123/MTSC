package cn.edu.fudan.mtsc;

import cn.edu.fudan.mtsc.common.Metadata;
import cn.edu.fudan.mtsc.common.MetadataGet;
import cn.edu.fudan.mtsc.entity.QueryData;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

/**
 * Created by sherry on 18-11-27.
 */
public class Decompress {

    public static void main(String[] args) {
        if (args.length == 1) {
            String filename = args[0];
            // 1.get metadata of all datasets
            List<Metadata> metadataList = MetadataGet.getInputDataMetadata("data/info.txt");
            for (Metadata metadata : metadataList) {
                if (metadata.getDataSet().equals(filename)) {
                    // decompress
                    int size = metadata.getSize();
                    int length = metadata.getLength();
                    QueryData queryData = new QueryData(metadata.getCompressedFilePath(), size, length);
                    double[][] decompress = queryData.queryAll();
                    writeToFile(decompress, metadata.getDecompressFilePath());
                    System.out.println("Finish decompress " + metadata.getDataSet());
                    System.out.println("Decompress path: " + metadata.getDecompressFilePath());
                    break;
                }
            }
        } else if (args.length == 2) {
            String dataset = args[0];
            int signalId = Integer.parseInt(args[1]);
            // 1.get metadata of all datasets
            List<Metadata> metadataList = MetadataGet.getInputDataMetadata("data/info.txt");
            for (Metadata metadata : metadataList) {
                if (metadata.getDataSet().equals(dataset)) {
                    // decompress
                    int size = metadata.getSize();
                    int length = metadata.getLength();
                    if (signalId >= size) {
                        System.out.println(dataset + " doesn't contain signal " + signalId);
                        System.exit(0);
                    }
                    QueryData queryData = new QueryData(metadata.getCompressedFilePath(), size, length);
                    double[] decompress = queryData.query(signalId);
                    String decompressFilePath = metadata.getDecompressFilePath();
                    int dot = decompressFilePath.lastIndexOf(".");
                    decompressFilePath = decompressFilePath.substring(0, dot) + "_" + signalId + decompressFilePath.substring(dot);
                    writeToFile(decompress, decompressFilePath);
                    System.out.println("Finish decompress the signal " + signalId + " of " + metadata.getDataSet());
                    System.out.println("Decompress path: " + decompressFilePath);
                    break;
                }
            }
        } else {
            System.out.println("required one or two parameters: dataset [signalId]");
            System.exit(0);
        }

    }

    private static void writeToFile(double[][] decompress, String decompressFilePath) {
        int size = decompress.length, length = decompress[0].length;
        try {
            File file = new File(decompressFilePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            for (int j = 0; j < length; j++) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < size; i++) {
                    if (i > 0) stringBuilder.append(",");
                    stringBuilder.append(decompress[i][j]);
                }
                stringBuilder.append("\n");
                writer.append(stringBuilder.toString());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(double[] decompress, String decompressFilePath) {
        try {
            File file = new File(decompressFilePath);
            file.getParentFile().mkdirs();
            FileWriter writer = new FileWriter(file);
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < decompress.length; j++) {
                if (j > 0) stringBuilder.append("\n");
                stringBuilder.append(decompress[j]);
            }
            writer.write(stringBuilder.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
