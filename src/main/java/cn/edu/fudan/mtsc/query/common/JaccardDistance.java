package cn.edu.fudan.mtsc.query.common;

import java.util.List;

/**
 * Created by sherry on 19-12-25.
 */
public class JaccardDistance {

    public static double compute(List<Integer> seg1, List<Integer> seg2) {
        if (seg1.size() == 0 && seg2.size() == 0) return 1;
        int common = 0;
        for (int i : seg1) {
            for (int j : seg2) {
                if (i == j) common++;
            }
        }
        return 1.0 * common / (seg1.size() + seg2.size() - common);
    }

}
