package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

/**
 * Special case max clique finder for |V| == 2
 * @author Steve Ash
 */
public class Special2Finder<T> implements MaxCliqueFinder<T> {

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        assert (elements.size() == 2);

        Iterator<T> iter = elements.iterator();
        T a = iter.next();
        T b = iter.next();

        double wab = weigher.weigh(a, b);
        if (wab > 0) {
            return new Clique<>(ImmutableSet.of(a, b), wab);
        }
        return new Clique<>(ImmutableSet.of(a), 0);
    }
}
