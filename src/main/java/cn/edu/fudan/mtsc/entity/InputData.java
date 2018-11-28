package cn.edu.fudan.mtsc.entity;

import cn.edu.fudan.mtsc.common.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by sherry on 17-3-23.
 */
public class InputData {

    public int size, totalLen;
    private String filename;
    private double[][] allData;

    private double maximumVal = -100000000;
    private double minimumVal = 100000000;

    public InputData(String filename) {
        this.filename = filename;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            size = line.split(",").length;
            totalLen = 1;
            String[] data = line.split(",");
            for(int i = 0; i < Integer.min(size, data.length); i++){
                double val = Double.valueOf(data[i]);
                if (val > maximumVal) maximumVal = val;
                if (val < minimumVal) minimumVal = val;
            }
            while((line = br.readLine()) != null){
                data = line.split(",");
                for(int i = 0; i < Integer.min(size, data.length); i++){
                    double val = Double.valueOf(data[i]);
                    if (val > maximumVal) maximumVal = val;
                    if (val < minimumVal) minimumVal = val;
                }
                totalLen++;
            }
            if (totalLen == 0 || size == 0) {
                System.out.println("Input can't be empty.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readAllValue(filename);
    }

    // read part from allData in RAM, length=w per time
    public double[][] readValue(int file_pt, int w) {
        if (file_pt + w >= totalLen) w = totalLen - file_pt;
        double[][] val = new double[size][w];
        int row = file_pt;
        while (row < file_pt + w) {
            for (int i = 0; i < size; i++) {
                val[i][row - file_pt] = allData[i][row];
            }
            row++;
        }
        return val;

    }

    // read all data from file
    private void readAllValue(String filename) {
        this.filename = filename;
        double[][] val = new double[totalLen][size];
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            String[] data = line.split(",");
            if (size != data.length) {
                System.out.println("Input data is illegal");
                System.exit(0);
            }
            totalLen = 0;
            for(int i = 0; i<Integer.min(this.size, data.length); i++){
                val[totalLen][i] = Double.valueOf(data[i]);
            }
            totalLen++;
            while((line = br.readLine()) != null){
                data = line.split(",");
                for(int i = 0; i<Integer.min(this.size, data.length); i++){
                    val[totalLen][i] = Double.valueOf(data[i]);
                }
                totalLen++;
            }
            if (totalLen == 0 || this.size == 0) {
                System.out.println("Input can't be empty.");
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double[][] t_val = new double[this.size][totalLen];
        for(int i = 0; i < totalLen; i++) {
            for(int j = 0; j < this.size; j++) {
                t_val[j][i] = val[i][j];
            }
        }
        this.allData = t_val;
    }

    public Pair<double[][], double[]> normValue(double[][] val) {
        double[] offsetValue = new double[size];
        int siz = val.length, length = 0;
        if(val.length > 0) length = val[0].length;
        for(int i = 0; i < siz; i++) {
            double sum = 0.0;
            for(int j = 0; j < length; j++) {
                sum += val[i][j];
            }
            double avg = sum / length;
            for(int j = 0; j < length; j++) {
                val[i][j] -= avg;
            }
            offsetValue[i] = avg;
        }
        return new Pair<>(val, offsetValue);
    }

    public double getMax() {
        return maximumVal;
    }

    public double getMin() {
        return minimumVal;
    }

    public String getFilename() {
        return filename;
    }

    public double[][] getAllData() {
        return allData;
    }
}