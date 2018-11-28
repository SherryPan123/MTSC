package cn.edu.fudan.mtsc.result;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherry on 17-4-9.
 */
public class SegmentInfo implements Serializable {

    private int length;
    private List<GroupInfo> groupInfos;
    private List<Pair<Integer, List<HorizontalBucket>>> multiSingleBuckets; //signalId, buckets

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<GroupInfo> getGroupInfos() {
        return groupInfos;
    }

    public void setGroupInfos(List<GroupInfo> groupInfos) {
        this.groupInfos = groupInfos;
    }

    public List<Pair<Integer, List<HorizontalBucket>>> getMultiSingleBuckets() {
        return multiSingleBuckets;
    }

    public void setMultiSingleBuckets(List<Pair<Integer, List<HorizontalBucket>>> multiSingleBuckets) {
        this.multiSingleBuckets = multiSingleBuckets;
    }

}
