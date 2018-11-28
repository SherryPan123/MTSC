package cn.edu.fudan.mtsc.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.IllegalFormatException;

/**
 * Created by sherry on 17-4-11.
 */
public class Metadata {

    private String dataSet;
    private String filePath;
    private String compressedFilePath;
    private String decompressFilePath;
    private int size; //signal number
    private int length;
    private float eps;

    public Metadata(String dataSet, String filePath, String compressedFilePath, String decompressFilePath, int size, int length, float eps) {
        this.dataSet = dataSet;
        this.filePath = filePath;
        this.compressedFilePath = compressedFilePath;
        this.decompressFilePath = decompressFilePath;
        this.size = size;
        this.length = length;
        this.eps = eps;
    }

    public int getSize() {
        return size;
    }

    public int getLength() {
        return length;
    }

    public float getEps() {
        return eps;
    }

    public String getDataSet() {
        return dataSet;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCompressedFilePath() {
        return compressedFilePath;
    }

    public String getDecompressFilePath() {
        return decompressFilePath;
    }
}
