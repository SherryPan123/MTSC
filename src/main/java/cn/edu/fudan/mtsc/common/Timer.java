package cn.edu.fudan.mtsc.common;

/**
 * Created by sherry on 17-3-26.
 */
public class Timer {

    long startTime, endTime;

    public Timer() {
        startTime = -1;
        endTime = -1;
        timeBegin();
    }

    private void timeBegin() {
        startTime = System.currentTimeMillis();
    }

    public void timeEnd() {
        endTime = System.currentTimeMillis();
    }

    public long timeDuring() {
        if (startTime == -1) return 0;
        if (endTime == -1) timeEnd();
        return endTime-startTime;
    }

    public String timeDuringStr() {
        if (startTime == -1) return "";
        if (endTime == -1) timeEnd();
        return String.valueOf(endTime-startTime);
    }

}
