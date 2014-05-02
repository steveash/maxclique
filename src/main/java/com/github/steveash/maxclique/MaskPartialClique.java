package com.github.steveash.maxclique;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Partial clique represents all of the members in the set and the ones left to check
 * This implementation uses long masks to represent the members compactly; the lowest order position
 * corresponds to vertex 0, the next bit to vertex 1, etc.
 * @author Steve Ash
 */
public class MaskPartialClique {

    public static final MaskPartialClique nullInstance = new MaskPartialClique(0, 0, -1);

    private final long members;
    private final double memberWeight;
    private long leftToCheck;

    public MaskPartialClique(long members, long leftToCheck, double memberWeight) {
        this.members = members;
        this.leftToCheck = leftToCheck;
        this.memberWeight = memberWeight;
    }

    public boolean hasMoreToCheck() {
        return leftToCheck > 0;
    }

    public int popNextToCheck() {
        int indexToCheck = Long.numberOfTrailingZeros(leftToCheck);
        this.leftToCheck = clearLowestBit(leftToCheck);
        return indexToCheck;
    }

    private long clearLowestBit(long word) {
        long valueOfNextToCheck = Long.lowestOneBit(word);
        word &= ~valueOfNextToCheck;
        return word;
    }

    public int size() {
        return Long.bitCount(this.members);
    }

    public long copyOfMembersPlus(int appendThis) {
        return this.members | (1 << appendThis);
    }

    public long getMembers() { return members; }

    public long getLeftToCheck() { return leftToCheck; }

    public double getMemberWeight() { return memberWeight; }

    public <T> double candidateJoinWeight(int candidate, Graph<T> g) {
        long membersLeft = members;
        double weightDelta = 0;

        while (membersLeft != 0) {
            int indexToCheck = Long.numberOfTrailingZeros(membersLeft);

            double weight = g.weight(candidate, indexToCheck);
            if (weight <= 0)
                return Double.NEGATIVE_INFINITY;

            weightDelta += weight;
            membersLeft = clearLowestBit(membersLeft);
        }

        return weightDelta;
    }

    public <T> Clique<T> convertToClique(Graph<T> g) {
        Builder<T> b = ImmutableSet.builder();
        for (int i = 0; i < g.size(); i++) {
            long value = (1 << i);
            if ((this.members & value) != 0) {
                b.add(g.vertexAt(i));
            }
        }
        return new Clique<>(b.build(), memberWeight);
    }
}
