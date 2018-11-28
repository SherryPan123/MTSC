package cn.edu.fudan.mtsc.group;

import cn.edu.fudan.mtsc.entity.InputData;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-5-2.
 */
public class GroupExecutor {

    // data
    double[][] val;
    int size;
    InputData inputData;
    // parameter
    int w;
    double eps = 3;
    // group result
    public List<GroupResultItem> groups; // groupNum:  number of groups
    public double[] offsetValue;
    public ApcaBase apcaBase; // in MTSC-mc&MTSC-inc
    public Statistic statistic;

    public GroupExecutor() {}

    public void init() {
        statistic = new Statistic();
        size = inputData.size;
        groups = new ArrayList<>();
    }

    public void run() {}

}
