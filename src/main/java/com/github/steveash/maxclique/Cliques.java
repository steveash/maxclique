package com.github.steveash.maxclique;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

/**
 * @author Steve Ash
 */
public class Cliques {
    private Cliques() { }

    public static <T> Clique<T> findMaximum(Collection<T> nodes, Weigher<T> edgeWeigher) {
        switch (nodes.size()) {
            case 0:
                throw new IllegalArgumentException("Cannot find a clique with an empty graph");
            case 1:
                return new Clique<>(ImmutableSet.<T>of(nodes.iterator().next()), 0);
            // TODO probs should add the easy 2 special case too; already megamorphic anyways
            case 3:
                return new Special3MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            case 4:
                return new Special4MaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
            default:
                return new GeneralMaxCliqueFinder<T>().findMaximum(nodes, edgeWeigher);
        }
    }
}
