package com.github.steveash.maxclique;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;

/**
 * @author Steve Ash
 */
public class SetUtils {

    private static final IntOpenHashSet empty = new IntOpenHashSet();

    public static IntSet emptySet() {
        return empty;
    }

    public static IntOpenHashSet copyAndAdd(IntOpenHashSet set, int vToAdd) {
        IntOpenHashSet copy = set.clone();
        copy.add(vToAdd);
        return copy;
    }

    public static IntOpenHashSet copyAndRemove(IntOpenHashSet set, int vToRemove) {
        IntOpenHashSet copy = set.clone();
        copy.remove(vToRemove);
        return copy;
    }
}
