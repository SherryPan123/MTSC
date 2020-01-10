package cn.edu.fudan.mtsc.group;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.entity.InputData;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-4-4.
 */
public class StarGroupExecutor extends GroupExecutor {

    public Statistic statistic;

    private List<Integer>[] edges; // 邻接表
    private boolean[][] graph;

    ApcaDegree apcaDegree;

    public ApcaDegree getApcaDegree() {
        return apcaDegree;
    }

    public StarGroupExecutor(InputData inputData, int file_pt, int w, double eps) {
        this.w = w;
        val = inputData.readValue(file_pt, w);
        Pair<double[][], double[]> res = inputData.normValue(val);
        this.val = res.getFirst();
        int size = this.val.length;
        this.offsetValue = res.getSecond();
        this.inputData = inputData;
        this.eps = eps;
        this.graph = new boolean[size][size];
    }

    public void init() {
        statistic = new Statistic();
        size = inputData.size;
        groups = new ArrayList<>();
        edges = new ArrayList[size];
    }

    public void run() {
        int[] degree = new int[size];
        calculateMatrix(degree);
        apcaDegree = new ApcaDegree(val);
        apcaDegree.setEps(eps);
        boolean[] isChosen = new boolean[size];
        int groupId = 0;
        // get groups by degrees
        while (true) {
            int id = findMaximumDegree(degree, isChosen);
            if (id == -1) break;
            // construct a new group
            List<Integer> ids = new ArrayList<>();
            ids.add(id);
            isChosen[id] = true;
            for (int j=0; j<edges[id].size(); j++) {
                int next_id = edges[id].get(j);
                if (isChosen[next_id]) continue;
                ids.add(next_id);
                isChosen[next_id] = true;
                // degree of adjacent point - 1
                for (int k=0; k<edges[next_id].size(); k++) {
                    int adj_id = edges[next_id].get(k);
                    degree[adj_id]--;
                }
            }
            groups.add(new GroupResultItem(groupId++, ids));
        }
        apcaDegree.setGroups(groups);
        apcaDegree.compressBases();

        // statistic information
        apcaDegree.computeCompressRate(statistic);
    }

    private int findMaximumDegree(int[] degree, boolean[] isChosen) {
        int id = -1;
        int mxDegree = -1;
        for (int i=0; i<size; i++) {
            if (isChosen[i]) continue;
            if (degree[i] > mxDegree) {
                mxDegree = degree[i];
                id = i;
            }
        }
        return id;
    }

    private void calculateMatrix(int[] degree) {
        for (int i=0; i<size; i++) {
            edges[i] = new ArrayList<>();
        }
        for(int x=0; x<size; x++) {
            for(int y=x; y<size; y++) {
                boolean flag = true;
                for(int i=0; i<w; i++) {
                    if(Math.abs(val[x][i]-val[y][i]) > eps) {
                        flag = false;
                        break;
                    }
                }
                graph[x][y] = graph[y][x] = flag;
                if (flag) {
                    edges[x].add(y);
                    edges[y].add(x);
                    degree[x]++;
                    degree[y]++;
                }
            }
        }
    }


    public boolean[][] getGraph() {
        return graph;
    }

}

