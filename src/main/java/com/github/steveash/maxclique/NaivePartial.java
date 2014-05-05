package com.github.steveash.maxclique;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Partial clique represents all of the members in the set and the ones left to check
 * This implementation uses long masks to represent the members compactly; the lowest order position
 * corresponds to vertex 0, the next bit to vertex 1, etc.
 * @author Steve Ash
 */
public class NaivePartial {

    public static final NaivePartial nullInstance = new NaivePartial(0, 0, -1);

    private final int members;
    private final double memberWeight;
    private int leftToCheck;

    public NaivePartial(int members, int leftToCheck, double memberWeight) {
        this.members = members;
        this.leftToCheck = leftToCheck;
        this.memberWeight = memberWeight;
    }

    public boolean hasMoreToCheck() {
        return leftToCheck > 0;
    }

    public int popNextToCheck() {
        int indexToCheck = Integer.numberOfTrailingZeros(leftToCheck);
        this.leftToCheck = clearLowestBit(leftToCheck);
        return indexToCheck;
    }

    private int clearLowestBit(int word) {
        int valueOfNextToCheck = Integer.lowestOneBit(word);
        word &= ~valueOfNextToCheck;
        return word;
    }

    public int size() {
        return Integer.bitCount(this.members);
    }

    public int copyOfMembersPlus(int appendThis) {
        return this.members | (1 << appendThis);
    }

    public double getMemberWeight() { return memberWeight; }

    public <T> double candidateJoinWeight(int candidate, Graph<T> g) {
        int membersLeft = members;
        double weightDelta = 0;

        while (membersLeft != 0) {
            int indexToCheck = Integer.numberOfTrailingZeros(membersLeft);

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
            int value = (1 << i);
            if ((this.members & value) != 0) {
                b.add(g.vertexAt(i));
            }
        }
        return new Clique<>(b.build(), memberWeight);
    }
}
