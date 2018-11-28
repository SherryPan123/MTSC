package cn.edu.fudan.mtsc.common.apca;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-3-17.
 */
public class APCA {

    public static List<HorizontalBucket> runSinglePCA(double[] single, double eps) {
        List<HorizontalBucket> hbuckets = new ArrayList<>();
        int length = single.length;
        if(length == 0) return new ArrayList<>();
        double max = single[0], min = single[0];
        int posEnd = 0; // end of sliding window
        for(int i=1; i<length; i++) {
            if(single[i] < min || single[i] > max){
                double tmpmax = Double.max(max, single[i]);
                double tmpmin = Double.min(min, single[i]);
                // double absAvg = Math.abs((tmpmax + tmpmin) / 2);
                if((tmpmax - tmpmin) > 2 * eps){  // /absAvg
                    hbuckets.add(new HorizontalBucket((max + min) / 2, posEnd));
                    posEnd = i;
                    max = min = single[i];
                } else{
                    min = Double.min(single[i], min);
                    max = Double.max(single[i], max);
                    posEnd++;
                }
            }
            else{
                posEnd++;
            }
        }
        hbuckets.add(new HorizontalBucket((max + min) / 2, posEnd));
        return hbuckets;
    }

}
