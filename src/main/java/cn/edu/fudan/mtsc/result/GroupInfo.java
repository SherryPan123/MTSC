package cn.edu.fudan.mtsc.result;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;
import cn.edu.fudan.mtsc.group.GroupResultItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sherry on 17-4-9.
 */
public class GroupInfo implements Serializable {

    private GroupResultItem groupResultItem;
    private List<HorizontalBucket> baseBuckets;
    private List<Pair<Integer, Double>> signalIdAndOffsets;

    public GroupInfo(GroupResultItem groupResultItem, List<HorizontalBucket> baseBuckets, List<Pair<Integer, Double>> signalIdAndOffsets) {
        this.groupResultItem = groupResultItem;
        this.baseBuckets = baseBuckets;
        this.signalIdAndOffsets = signalIdAndOffsets;
    }

    public List<HorizontalBucket> getBaseBuckets() {
        return baseBuckets;
    }

    public List<Pair<Integer, Double>> getSignalIdAndOffsets() {
        return signalIdAndOffsets;
    }

}
