package cn.edu.fudan.mtsc.result;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;
import cn.edu.fudan.mtsc.group.ApcaBase;
import cn.edu.fudan.mtsc.group.EucGroupExecutor;
import cn.edu.fudan.mtsc.group.GroupResultItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-5-3.
 */
public class CompressRepresentationAdd {

    private CompressRepresentation compressRepresentation;

    public CompressRepresentationAdd() {
        compressRepresentation = new CompressRepresentation();
    }

    public CompressRepresentation addCompressedData(int len, EucGroupExecutor eucGroupExecutor) {
        if (compressRepresentation == null) {
            compressRepresentation = new CompressRepresentation();
        }
        SegmentInfo segmentInfo = new SegmentInfo();
        segmentInfo.setLength(len);
        ApcaBase apcaBase = eucGroupExecutor.apcaBase;
        List<GroupInfo> groupInfos = new ArrayList<>();
        List<GroupResultItem> groupResultItemList = apcaBase.groups;
        for (int i=0; i<groupResultItemList.size(); i++) {
            GroupResultItem groupResultItem = groupResultItemList.get(i);
            List<HorizontalBucket> baseBuckets = apcaBase.multiBaseBuckets.get(i);
            List<Pair<Integer, Double>> signalIdAndOffsets = new ArrayList<>();
            for (Integer id : groupResultItem.getIds()) {
                Pair<Integer, Double> myPair = new Pair(id, eucGroupExecutor.offsetValue[id]);
                signalIdAndOffsets.add(myPair);
            }
            GroupInfo groupInfo = new GroupInfo(groupResultItem, baseBuckets, signalIdAndOffsets);
            groupInfos.add(groupInfo);
        }
        segmentInfo.setGroupInfos(groupInfos);
        List<Pair<Integer, List<HorizontalBucket>>> multisingleBuckets = new ArrayList<>();
        if (apcaBase.multiSingleBuckets != null) {
            for (Pair<Integer, List<HorizontalBucket>> singleBuckets : apcaBase.multiSingleBuckets) {
                int id = singleBuckets.getFirst();
                double sub_value = eucGroupExecutor.offsetValue[id];
                List<HorizontalBucket> add_Buckets = new ArrayList<>();
                for (HorizontalBucket hb : singleBuckets.getSecond()) {
                    add_Buckets.add(new HorizontalBucket(hb.getValue() + sub_value, hb.getEnd()));
                }
                multisingleBuckets.add(new Pair<>(id, add_Buckets));
            }
        }
        segmentInfo.setMultiSingleBuckets(multisingleBuckets);

        compressRepresentation.addSegmentInfo(segmentInfo);
        return compressRepresentation;
    }

    public void serializeCompressedRepresentation(String compressedFilePath) {
        try {
            File file = new File(compressedFilePath);
            file.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(compressRepresentation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
