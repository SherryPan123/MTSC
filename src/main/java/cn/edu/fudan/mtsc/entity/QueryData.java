package cn.edu.fudan.mtsc.entity;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.apca.HorizontalBucket;
import cn.edu.fudan.mtsc.result.CompressRepresentation;
import cn.edu.fudan.mtsc.result.GroupInfo;
import cn.edu.fudan.mtsc.result.SegmentInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by sherry on 17-4-9.
 */
public class QueryData {

    private String path;
    private int size;
    private int length;

    public QueryData(String path, int size, int length) {
        this.path = path;
        this.size = size;
        this.length = length;
    }

    public double[] query(int signalId) {
        File file = new File(path);
        double[] val = new double[length];
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))) {
            CompressRepresentation compressRepresentation = (CompressRepresentation) out.readObject();
            List<SegmentInfo> segmentInfos = compressRepresentation.getSegmentInfos();
            int data_cnt = 0;
            for (SegmentInfo segmentInfo : segmentInfos) {
                // cur_pt ~ cur_pt + segmentInfo.getLength()
                boolean isFind = false;
                // find in single signals
                List<Pair<Integer, List<HorizontalBucket>>> multiSingleBuckets = segmentInfo.getMultiSingleBuckets();
                for (Pair<Integer, List<HorizontalBucket>> singleBuckets: multiSingleBuckets) {
                    if (singleBuckets.getFirst() == signalId) {
                        isFind = true;
                        double[] segment_data = new double[segmentInfo.getLength()];
                        data_cnt = getFromSingleBucket(singleBuckets, segment_data, val, data_cnt);
                        break;
                    }
                }
                if (isFind) continue;
                // find in group
                List<GroupInfo> groupInfos = segmentInfo.getGroupInfos();
                for (GroupInfo groupInfo : groupInfos) {
                    List<Pair<Integer, Double>> signalIdAndOffsets = groupInfo.getSignalIdAndOffsets();
                    List<HorizontalBucket> baseBuckets = groupInfo.getBaseBuckets();
                    for (Pair<Integer, Double> signal : signalIdAndOffsets) {
                        if (signal.getFirst() == signalId) {
                            isFind = true;
                            double[] segment_data = new double[segmentInfo.getLength()];
                            data_cnt = getFromBaseBucket(baseBuckets, signal, segment_data, val, data_cnt);
                            break;
                        }
                    }
                    if (isFind) break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return val;
    }

    public double[][] queryAll() {
        File file = new File(path);
        double[][] data = new double[size][length];
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))) {
            CompressRepresentation compressRepresentation = (CompressRepresentation) out.readObject();
            List<SegmentInfo> segmentInfos = compressRepresentation.getSegmentInfos();
            int data_cnt = 0;
            for (SegmentInfo segmentInfo : segmentInfos) {
                int w = segmentInfo.getLength();
                // single signals
                List<Pair<Integer, List<HorizontalBucket>>> multiSingleBuckets = segmentInfo.getMultiSingleBuckets();
                int cur_cnt = data_cnt;
                for (Pair<Integer, List<HorizontalBucket>> singleBuckets: multiSingleBuckets) {
                    int signalId = singleBuckets.getFirst();
                    data_cnt = getFromSingleBucket(singleBuckets, new double[w], data[signalId], cur_cnt);
                }
                // in group
                List<GroupInfo> groupInfos = segmentInfo.getGroupInfos();
                for (GroupInfo groupInfo : groupInfos) {
                    List<Pair<Integer, Double>> signalIdAndOffsets = groupInfo.getSignalIdAndOffsets();
                    List<HorizontalBucket> baseBuckets = groupInfo.getBaseBuckets();
                    for (Pair<Integer, Double> signal : signalIdAndOffsets) {
                        int signalId = signal.getFirst();
                        data_cnt = getFromBaseBucket(baseBuckets, signal, new double[w], data[signalId], cur_cnt);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }

    private int getFromSingleBucket(Pair<Integer, List<HorizontalBucket>> singleBuckets,
                                    double[] segment_data, double[] val, int data_cnt) {
        int segment_data_cnt = 0;
        int end_seg_pt, start_seg_pt = 0;
        for (HorizontalBucket horizontalBucket : singleBuckets.getSecond()) {
            end_seg_pt = horizontalBucket.getEnd();
            double value = horizontalBucket.getValue();
            for (int i = start_seg_pt; i <= end_seg_pt; i++) {
                segment_data[segment_data_cnt++] = value;
            }
            start_seg_pt = end_seg_pt + 1;
        }
        for (int i = 0; i < segment_data_cnt; i++) {
            val[data_cnt++] = segment_data[i];
        }
        return data_cnt;
    }

    private int getFromBaseBucket(List<HorizontalBucket> baseBuckets, Pair<Integer, Double> signal,
                                  double[] segment_data, double[] val, int data_cnt) {
        int segment_data_cnt = 0;
        int end_seg_pt = 0, start_seg_pt = 0;
        for (HorizontalBucket bucket : baseBuckets) {
            end_seg_pt = bucket.getEnd();
            double value = bucket.getValue();
            for (int i=start_seg_pt; i<=end_seg_pt; i++) {
                segment_data[segment_data_cnt++] = signal.getSecond() + value;
            }
            start_seg_pt = end_seg_pt+1;
        }
        for (int i = 0; i < segment_data_cnt; i++) {
            val[data_cnt++] = segment_data[i];
        }
        return data_cnt;
    }


}
