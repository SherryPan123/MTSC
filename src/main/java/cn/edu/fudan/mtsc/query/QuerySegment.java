package cn.edu.fudan.mtsc.query;

import cn.edu.fudan.mtsc.result.CompressRepresentation;
import cn.edu.fudan.mtsc.result.SegmentInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * Created by sherry on 19-12-25.
 */
public class QuerySegment {

    private String path;

    public QuerySegment(String compressedFilePath) {
        this.path = compressedFilePath;
    }

    public List<SegmentInfo> getSegmentInfo() {
        File file = new File(path);
        try(ObjectInputStream out = new ObjectInputStream(new FileInputStream(file))) {
            CompressRepresentation compressRepresentation = (CompressRepresentation) out.readObject();
            List<SegmentInfo> segmentInfos = compressRepresentation.getSegmentInfos();
            return segmentInfos;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
