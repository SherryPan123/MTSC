package cn.edu.fudan.mtsc.common.maximumClique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sherry on 17-3-23.
 */
public class MaximumCliqueProblem {

    private boolean[][] graph;
    private int size;

    // temp variable for medium computing
    private int[] mc;
    private int ans;
    private boolean found;
    private int[] len;  //set S
    private int[][] conn;
    private List<Integer> path_mc;

    public MaximumCliqueProblem(boolean[][] graph) {
        this.size = graph.length;
        this.graph = graph;
        mc = new int[size];
        len = new int[size+1];
        conn = new int[size][size];
    }

    public void max_cluster() {
        ans = 0;
//        System.out.println("最大团");
        path_mc = new ArrayList<>();
        for (int i=size-1; i>=0; i--) {
            found = false;
            len[1] = 0;
            for (int j=i+1; j<size; j++) {
                if (graph[i][j]) {
                    conn[1][len[1]++] = j;
                }
            }
            dfs(1);
            if (found) path_mc.add(i);
//            System.out.print(i+" "+path_mc.size()+",");
//            if(i%10==0) System.out.println();
            mc[i] = ans;
            // todo: tmp cut
            //if (size>150 && size-i>100) break;
//            if (size>250 && size-i>100) break;
        }
//        System.out.println();
//        System.out.println(path_mc.size()+"    "+path_mc);
    }

    private void dfs(int curSize) {
        if (len[curSize] == 0) {
            if (curSize > ans) {
                ans = curSize;
                found = true;
                path_mc.clear();
            }
            return;
        }
        for (int k=0; k<len[curSize]&&(!found); k++) {
            if (curSize+len[curSize]-k <= ans) break; // 剪枝1：如果 U 集合中的点的数量+1（选择 ui 加入 U 集合中）+Si 中所有 ui 后面的点的数量 ≤ 当前最优值，不用再 DFS 了
            int i = conn[curSize][k];
            if (curSize+mc[i] <= ans) break; // 剪枝2：如果 U 集合中的点的数量+1（理由同上）+[ui, n]这个区间中能构成的最大团的顶点数量 ≤当前最优值，不用再 DFS了
            len[curSize+1]=0;
            for (int j=k+1; j<len[curSize]; j++) {
                if (graph[i][conn[curSize][j]]) {
                    conn[curSize+1][len[curSize+1]++] = conn[curSize][j];
                }
            }
            dfs(curSize+1);
            if (found) {
                path_mc.add(i);
                return; //剪枝3：如果 DFS 到最底层，我们能够更新答案，不用再 DFS 了，结束整个 DFS 过程，也不再返回上一层继续DFS了
            }
        }
    }

    public List<Integer> getPath_mc() {
        return path_mc;
    }
}
