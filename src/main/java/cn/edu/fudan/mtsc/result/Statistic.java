package cn.edu.fudan.mtsc.result;

/**
 * Created by sherry on 17-3-29.
 */
public class Statistic {

    // statistic variable
    public String staFilename;
    public double numerator, denominator;
    public long sumTime;

    public Statistic() {}

    public String toString() {
        return "compress rate: "+String.valueOf(numerator / denominator)+"\tcompress time: "+sumTime;
    }

}
