package cn.edu.fudan.mtsc;

import cn.edu.fudan.mtsc.common.MetadataGet;
import cn.edu.fudan.mtsc.entity.InputData;
import cn.edu.fudan.mtsc.common.Metadata;
import cn.edu.fudan.mtsc.common.Timer;
import cn.edu.fudan.mtsc.group.EucGroupExecutor;
import cn.edu.fudan.mtsc.result.CompressRepresentationAdd;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.List;

/**
 * Created by sherry on 18-11-25.
 */
public class Compress {

    public static void main(String[] args) {
        // 1.get metadata of all datasets
        List<Metadata> metadataList = MetadataGet.getInputDataMetadata("data/info.txt");

        for (Metadata metadata : metadataList) {
            // 2.read input data
            InputData inputData = new InputData(metadata.getFilePath());

            // initialize some parameters,
            double eps = metadata.getEps() * (inputData.getMax() - inputData.getMin()) / 100.0;
            int w = 50; // maximum width of each disjoint window
            double numerator = 0.0, denominator = 0.0; // compression ratio = numerator / denominator
            CompressRepresentationAdd compressRepresentationAdd = new CompressRepresentationAdd();

            System.out.println("Dataset: " + metadata.getDataSet());

            // 3.run compression algorithm
            Timer timer = new Timer();
            int file_pt = 0;
            while (file_pt < inputData.totalLen) {
                if (file_pt + w > inputData.totalLen) {
                    w = inputData.totalLen - file_pt;
                }
                EucGroupExecutor eucGroupExecutor = new EucGroupExecutor(inputData, file_pt, w, eps);
                eucGroupExecutor.init();
                eucGroupExecutor.run();
                Statistic statistic = eucGroupExecutor.statistic;
                // total compression rate
                numerator += statistic.numerator;
                denominator += statistic.denominator;
                compressRepresentationAdd.addCompressedData(w, eucGroupExecutor);
                file_pt += w;
            }
            compressRepresentationAdd.serializeCompressedRepresentation(metadata.getCompressedFilePath());
            System.out.println("Compression ratio: " + (numerator / denominator));
            System.out.println("Total time: " + timer.timeDuringStr() +" (ms)\n");
        }
    }

}
