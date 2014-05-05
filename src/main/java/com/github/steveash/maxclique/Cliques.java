package com.github.steveash.maxclique;

import java.util.Collection;

/**
 * @author Steve Ash
 */
public class Cliques {
    private Cliques() { }

    public static <T> Clique<T> findMaximum(Collection<T> nodes, Weigher<T> edgeWeigher) {
        return MaxCliqueFinderFacade.findMaximumBySize(nodes, edgeWeigher);
    }
}
