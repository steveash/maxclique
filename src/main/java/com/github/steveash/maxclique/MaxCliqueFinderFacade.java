package com.github.steveash.maxclique;

import java.util.Collection;

/**
 * @author Steve Ash
 */
public class MaxCliqueFinderFacade<T> implements MaxCliqueFinder<T> {
    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        return Cliques.findMaximum(elements, weigher);
    }
}
