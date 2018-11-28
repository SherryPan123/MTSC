package cn.edu.fudan.mtsc.group;

import cn.edu.fudan.mtsc.common.Pair;
import cn.edu.fudan.mtsc.common.Timer;
import cn.edu.fudan.mtsc.common.maximumClique.MaximumCliqueProblem;
import cn.edu.fudan.mtsc.entity.InputData;
import cn.edu.fudan.mtsc.result.Statistic;

import java.util.*;

/**
 * Created by sherry on 17-4-4.
 */
public class EucGroupExecutor extends GroupExecutor {

    private boolean[][] graph;
    private boolean[] isChosen;

    public EucGroupExecutor(InputData inputData, int file_pt, int w, double eps) {
        this.w = w;
        val = inputData.readValue(file_pt, w);
        Pair<double[][], double[]> res = inputData.normValue(val);
        this.val = res.getFirst();
        this.offsetValue = res.getSecond();
        this.inputData = inputData;
        this.eps = eps;
    }

    @Override
    public void init() {
        statistic = new Statistic();
        size = inputData.size;
        isChosen = new boolean[size];
        groups = new ArrayList<>();
    }

    @Override
    public void run() {
        calculateMatrix();
        doGrouping();
    }

    private void calculateMatrix() {
        graph = new boolean[size][size];
        //int sum = 0;
        for(int x = 0; x < size; x++) {
            for(int y = x + 1; y < size; y++) {
                boolean flag = true;
                for(int i = 0; i < w; i++) {
                    if(Math.abs(val[x][i] - val[y][i]) > 2 * eps) {
                        flag = false;
                        break;
                    }
                }
                graph[x][y] = graph[y][x] = flag;
                //if (flag) sum++;
            }
        }

//        System.out.print("{");
//        for (int i=0; i<size; i++) {
//            System.out.print("{");
//            for(int j=0; j<size; j++) {
//                if (j > 0) System.out.print(",");
//                System.out.print(graph[i][j]);
//            }
//            if (i < size-1) System.out.println("},");
//            else System.out.println("}");
//        }
//        System.out.println("edges' number and edge density: "+sum +" , "+(sum*2.0/(size*(size-1))));
    }

    private void doGrouping() {
        Timer runTime = new Timer();
        apcaBase = new ApcaBase(val);
        apcaBase.setEps(eps);
        isChosen = new boolean[size];
        while(true) {
            // find a connect graph
            List<Integer> points = findConnectPart(); //read from graph, isChosen
            if (points.size() == 0) break;
            dealWithConnectPart(points);
        }
        apcaBase.setGroups(groups);
        groups.sort((o1, o2) -> o2.getIds().size() - o1.getIds().size());
        apcaBase.compressBaseAndSingle();
        // statistic information
        apcaBase.computeCompressRate(statistic);
        statistic.sumTime += runTime.timeDuring();
    }

    private List<Integer> findConnectPart() {
        List<Integer> points = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (isChosen[i]) continue;
            bfs(points, i);
            if (points.size() == 1) {
                isChosen[i] = true;
                points.clear();
            } else {
                return points;
            }
        }
        return points;
    }

    private void bfs(List<Integer> points, int start) {
        boolean[] vis = new boolean[size];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        vis[start] = true;
        points.add(start);
        while(!queue.isEmpty()) {
            Integer cur = queue.remove();
            for (int i = 0; i < size; i++) {
                if((!vis[i]) && (!isChosen[i]) && graph[cur][i]) {
                    queue.add(i);
                    vis[i] = true;
                    points.add(i);
                    if(points.size() > 300) return;
                }
            }
        }
    }

    private void dealWithConnectPart(List<Integer> points) {
        // split some parts of connect graph
        List<Integer> leftPoints = points;
        int num = leftPoints.size();
        boolean[][] g;
        if (num < points.size()) {
            g = createNewGraph(leftPoints);
            points = leftPoints;
        } else {
            g = createNewGraph(points); //graph
        }
        // start to find maximum clique
        MaximumCliqueProblem mcp = new MaximumCliqueProblem(g);
        mcp.max_cluster();
        // delete maximum clique edges and points so that next connect part will be chosen
        List<Integer> chosenPointsId = mcp.getPath_mc();
        List<Integer> chosenPoints = new ArrayList<>();

        for (Integer aChosenPointsId : chosenPointsId) {
            int id = aChosenPointsId;
            int p = points.get(id);
            isChosen[p] = true;
            chosenPoints.add(p);
        }
        int groupNum = groups.size();
        groups.add(new GroupResultItem(groupNum++, chosenPoints));
    }

    private boolean[][] createNewGraph(List<Integer> points) {
        int psize = points.size();
        boolean[][] g = new boolean[psize][psize];
        for (int i=0; i<psize; i++) {
            for (int j=i; j<psize; j++) {
                g[i][j] = g[j][i] = graph[points.get(i)][points.get(j)];
            }
        }
        return g;
    }

}

