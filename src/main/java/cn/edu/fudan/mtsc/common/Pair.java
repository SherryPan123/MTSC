package cn.edu.fudan.mtsc.common;

import java.io.Serializable;

/**
 * Created by sherry on 17-2-25.
 */
public class Pair<L,R> implements Serializable {

    private L first;
    private R second;

    public Pair(L first, R second) {
        this.first = first;
        this.second = second;
    }

    public L getFirst() {
        return first;
    }

    public R getSecond() {
        return second;
    }

    public void setFirst(L first) {
        this.first = first;
    }

    public void setSecond(R second) {
        this.second = second;
    }

    @Override
    public int hashCode() { return first.hashCode() ^ second.hashCode(); }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.first.equals(pairo.getFirst()) &&
                this.second.equals(pairo.getSecond());
    }

}
