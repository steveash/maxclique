package com.github.steveash.maxclique;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Represents one weighted clique; weights are all positive
 * @author Steve Ash
 */
public class Clique<T> implements Iterable<T> {
    private static final Clique<Object> instance = new Clique<>(ImmutableSet.<Object>of(), 0);

    public static <T> Clique<T> nullClique() {
        return (Clique<T>) instance;
    }

    private final ImmutableSet<T> members;
    private final double totalWeight;

    public Clique(ImmutableSet<T> members, double totalWeight) {
        this.members = members;
        this.totalWeight = totalWeight;
    }

    @Override
    public Iterator<T> iterator() {
        return members.iterator();
    }

    public Set<T> members() { return members; }

    public double totalWeight() { return totalWeight; }

    public int size() {
        return members.size();
    }

    public boolean isEmpty() {
        return members.isEmpty();
    }

    public boolean isSingleMember() {
        return members.size() == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Clique clique = (Clique) o;

        if (!members.equals(clique.members)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return members.hashCode();
    }

    @Override
    public String toString() {
        return "Clique{" +
                "members=" + members +
                ", totalWeight=" + totalWeight +
                '}';
    }
}
