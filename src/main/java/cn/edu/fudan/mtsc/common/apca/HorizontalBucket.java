package cn.edu.fudan.mtsc.common.apca;

import java.io.Serializable;

/**
 * Created by sherry on 17-3-20.
 */
public class HorizontalBucket implements Serializable {

    private double value;
    private int posEnd;

    public HorizontalBucket(double value, int posEnd) {
        this.value = value;
        this.posEnd = posEnd;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getEnd() {
        return posEnd;
    }

    public void setEnd(int end) {
        this.posEnd = end;
    }

}
