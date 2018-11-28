package cn.edu.fudan.mtsc.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-4-9.
 */
public class CompressRepresentation implements Serializable {

    private List<SegmentInfo> segmentInfos;

    CompressRepresentation() {
        segmentInfos = new ArrayList<>();
    }

    public List<SegmentInfo> getSegmentInfos() {
        return segmentInfos;
    }

    void addSegmentInfo(SegmentInfo segmentInfo) {
        segmentInfos.add(segmentInfo);
    }
}
