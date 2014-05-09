package com.github.steveash.maxclique;

import java.util.Collection;

/**
 * @author Steve Ash
 */
public class Cliques {
    private Cliques() { }

    /**
     * Find the maximum edge weights clique for the graph.  The graph is represented by a set of T (verticies)
     * and a function that can produce edge weights given any two verticies
     * @param nodes
     * @param edgeWeigher
     * @param <T>
     * @return
     */
    public static <T> Clique<T> findMaximum(Collection<T> nodes, Weigher<T> edgeWeigher) {
        return MaxCliqueFinderFacade.<T>getInstance().findMaximum(nodes, edgeWeigher);
    }

    public static <T> Iterable<Clique<T>> findAllMaximums(Collection<T> nodes, Weigher<T> edgeWeigher) {
        MaxCliqueFinderFacade<T> finder = MaxCliqueFinderFacade.getInstance();
        return new AllMaxCliqueFinder<>(nodes, finder, edgeWeigher);
    }
}
