package com.github.steveash.maxclique;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

/**
 * @author Steve Ash
 */
public class Cliques {
    private Cliques() { }

    public static <T> Clique<T> findMaximum(Collection<T> nodes, Weigher<T> edgeWeigher) {
        int size = nodes.size();

        if (size > 32) {
            // but good luck getting it to run...
            return new GeneralMaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
        }

        switch (size) {
            case 0:
                throw new IllegalArgumentException("Cannot find a clique with an empty graph");
            case 1:
                return new Clique<>(ImmutableSet.<T>of(nodes.iterator().next()), 0);
            case 2:
                return new Special2MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            case 3:
                return new Special3MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            case 4:
                return new Special4MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            case 5:
                return new Special5MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            default:
                return new Special32MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
        }
    }
}
