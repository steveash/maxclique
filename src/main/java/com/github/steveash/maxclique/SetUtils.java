package com.github.steveash.maxclique;

import com.carrotsearch.hppc.IntOpenHashSet;

/**
 * @author Steve Ash
 */
public class SetUtils {

    private static final IntOpenHashSet empty = new IntOpenHashSet();

    public static IntOpenHashSet emptySet() {
        return new IntOpenHashSet();
    }

    public static IntOpenHashSet copyAndAdd(IntOpenHashSet set, int vToAdd) {
        IntOpenHashSet copy = set.clone();
        copy.add(vToAdd);
        return copy;
    }

    public static IntOpenHashSet copy(IntOpenHashSet set) {
        return set.clone();
    }

    public static IntOpenHashSet copyAndRemove(IntOpenHashSet set, int vToRemove) {
        IntOpenHashSet copy = set.clone();
        copy.remove(vToRemove);
        return copy;
    }

    public static IntOpenHashSet singletonSet(int v) {
        IntOpenHashSet set = new IntOpenHashSet(1);
        set.add(v);
        return set;
    }

    public static long addToMask(long mask, int vToAdd) {
        return mask | (long) (1 << vToAdd);
    }

    public static long removeFromMask(long mask, int vToRemove) {
        return mask & ~(1 << vToRemove);
    }

    public static boolean isMaskEmpty(long mask) {
        return mask == 0;
    }

    public static int nextToCheck(long mask) {
        return Long.numberOfTrailingZeros(mask);
    }

    public static long popNextToCheck(long mask) {
        return clearLowestBit(mask);
    }

    public static boolean isSubsetOf(long subsetOf, long superSet) {
        return ((subsetOf & superSet) == subsetOf);
    }

    public static long intersection(long maskForSetA, long maskForSetB) {
        return maskForSetA & maskForSetB;
    }

    public static long union(long maskForSetA, long maskForSetB) {
        return maskForSetA | maskForSetB;
    }

    private static long clearLowestBit(long word) {
        long valueOfNextToCheck = Long.lowestOneBit(word);
        word &= ~valueOfNextToCheck;
        return word;
    }
}
