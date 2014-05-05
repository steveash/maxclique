package com.github.steveash.maxclique;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.primitives.Doubles;

/**
 * @author Steve Ash
 */
public class BronPartial implements Comparable<BronPartial> {

    public static final BronPartial nullPartial = new BronPartial(SetUtils.emptySet(), 0);

    private final IntOpenHashSet candidate;
    private final double weight;

    public BronPartial(IntOpenHashSet candidate, double weight) {
        this.candidate = candidate;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    //@Nullable
    public BronPartial tryWith(int vToAdd, Graph graph) {
        double newWeight = this.weight;
        for (IntCursor cursor : candidate) {

            double newEdge = graph.weight(vToAdd, cursor.value);
            if (newEdge <= 0) return null;  // this path can't lead to a maximum

            newWeight += newEdge;
        }
        return new BronPartial(SetUtils.copyAndAdd(candidate, vToAdd), newWeight);
    }

    public <T> Clique<T> convertToClique(Graph<T> g) {
        return new Clique<>(g.verticiesForIndexes(this.candidate), weight);
    }

    @Override
    public int compareTo(BronPartial o) {
        return Doubles.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return "BronPartial{" +
                "candidate=" + candidate +
                ", weight=" + weight +
                '}';
    }
}
