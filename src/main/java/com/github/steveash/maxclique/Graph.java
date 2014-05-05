package com.github.steveash.maxclique;

import static com.github.steveash.maxclique.SetUtils.addToMask;

import com.carrotsearch.hppc.IntArrayDeque;
import com.carrotsearch.hppc.IntDeque;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.base.Preconditions;
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
    private final long[] neighborMasks;

    public Graph(Iterable<T> verticies, Weigher<T> edgeWeigher) {
        this.verticies = ImmutableList.copyOf(verticies);
        if (this.verticies.size() <= 64) {
            neighborMasks = new long[this.verticies.size()];
        } else {
            neighborMasks = null;
        }
        this.weights = buildMatrix(this.verticies, edgeWeigher, neighborMasks);
    }

    private static <T> WeightMatrix buildMatrix(ImmutableList<T> v, Weigher<T> weigher, long[] neighborMasks) {
        int n = v.size();
        WeightMatrix m = new WeightMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double w = weigher.weigh(v.get(i), v.get(j));
                m.set(i, j, w);

                if (neighborMasks != null && w > 0) {
                    neighborMasks[i] = addToMask(neighborMasks[i], j);
                    neighborMasks[j] = addToMask(neighborMasks[j], i);
                }
            }
        }
        return m;
    }

    public long allVerticiesAsMask() {
        assert verticies.size() <= 64;
        long mask = (1 << verticies.size()) - 1;
        return mask;
    }

    public long neighborsAsMask(int v) {
        Preconditions.checkState(neighborMasks != null, "cannot use neighbor masks with a graph > 64 verticies");
        return neighborMasks[v];
    }

    public IntOpenHashSet copyOfAllVerticies() {
        IntOpenHashSet set = new IntOpenHashSet(verticies.size());
        for (int i = 0; i < verticies.size(); i++) {
            set.add(i);
        }
        return set;
    }

    public IntOpenHashSet intersectionWithNeighbors(IntOpenHashSet source, int onlyIncludeNeighborsOf) {
        if (source.isEmpty()) return SetUtils.emptySet();

        IntOpenHashSet copy = new IntOpenHashSet(source.size());
        for (int i = 0; i < verticies.size(); i++) {
            if (onlyIncludeNeighborsOf == i) continue;
            if (weights.weight(onlyIncludeNeighborsOf, i) > 0) {
                if (source.contains(i)) {
                    copy.add(i);
                }
            }
        }
        return copy;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph[");
        for (int i = 0; i < size(); i++) {
            for (int j = i + 1; j < size(); j++) {
                double w = weight(i, j);
                if (w > 0) {
                    sb.append(String.format("\n%d -> %d = %.2f", i, j, w));
                }
            }
        }
        sb.append("\n]");
        return sb.toString();
    }
}
