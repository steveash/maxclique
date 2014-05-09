package com.github.steveash.maxclique;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

/**
 * @author Steve Ash
 */
public class MaxCliqueFinderFacade<T> implements MaxCliqueFinder<T> {

    private static final MaxCliqueFinderFacade<Object> instance = new MaxCliqueFinderFacade<>();

    @SuppressWarnings("unchecked")
    public static <T> MaxCliqueFinderFacade<T> getInstance() {
        return (MaxCliqueFinderFacade<T>) instance;
    }

    @Override
    public Clique<T> findMaximum(Collection<T> nodes, Weigher<T> edgeWeigher) {
        return findMaximumBySize(nodes, edgeWeigher);
    }

    public static <T> Clique<T> findMaximumBySize(Collection<T> nodes, Weigher<T> edgeWeigher) {
        int size = nodes.size();

        if (size > 64) {
            return new BronKerbosch1Finder<T>().findMaximum(nodes, edgeWeigher);
        }

        switch (size) {
            case 0:
                throw new IllegalArgumentException("Cannot find a clique with an empty graph");
            case 1:
                return new Clique<>(ImmutableSet.of(nodes.iterator().next()), 0);
            case 2:
                return new Special2Finder<T>().findMaximum(nodes, edgeWeigher);
            case 3:
                return new Special3Finder<T>().findMaximum(nodes, edgeWeigher);
            case 4:
                return new Special4Finder<T>().findMaximum(nodes, edgeWeigher);
            case 5:
                return new Special5Finder<T>().findMaximum(nodes, edgeWeigher);
            default:
                return new LongMaskBronKerbosch1Finder<T>().findMaximum(nodes, edgeWeigher);
        }
    }
}
