package com.github.steveash.maxclique;

import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntDeque;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Simple graph operations
 * @author Steve Ash
 */
public class Graph<T> {

    private final ImmutableList<T> verticies;
    private final WeightMatrix weights;

    public Graph(Iterable<T> verticies, Weigher<T> edgeWeigher) {
        this.verticies = ImmutableList.copyOf(verticies);
        this.weights = buildMatrix(this.verticies, edgeWeigher);
    }

    private static <T> WeightMatrix buildMatrix(ImmutableList<T> v, Weigher<T> weigher) {
        int n = v.size();
        WeightMatrix m = new WeightMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                m.set(i, j, weigher.weigh(v.get(i), v.get(j)));
            }
        }
        return m;
    }

    public IntSet copyOfAllVerticies() {
        IntOpenHashSet set = new IntOpenHashSet(verticies.size());
        for (int i = 0; i < verticies.size(); i++) {
            set.add(i);
        }
        return set;
    }

    public IntDeque neighborsExcluding(int vertex, IntSet toExclude) {
        IntDeque neighbors = new IntArrayDeque(verticies.size() - 1);

        for (int i = 0; i < verticies.size(); i++) {
            if (vertex == i) continue;              // skip myself
            if (toExclude.contains(i)) continue;    // skip the toExcludes

            if (weight(vertex, i) > 0) {
                neighbors.addLast(i);
            }
        }

        return neighbors;
    }

    public int neighborsExcludingAsMask(int vertex, int toExclude) {
        int mask = 0;
        for (int i = 0; i < verticies.size(); i++) {
            if (vertex == i) continue;              // skip myself

            int candidate = (1 << i);
            if ((candidate & ~toExclude) == 0) continue;

            if (weight(vertex, i) > 0) {
                mask |= (1 << i);
            }
        }
        return mask;
    }

    public ImmutableSet<T> verticiesForIndexes(IntSet indexes) {
        Builder<T> builder = ImmutableSet.builder();
        for (IntCursor index : indexes) {
            builder.add(verticies.get(index.value));
        }
        return builder.build();
    }

    public T vertexAt(int index) {
        return verticies.get(index);
    }

    public double weight(int a, int b) {
        return weights.weight(a, b);
    }

    public int size() {
        return verticies.size();
    }
}
