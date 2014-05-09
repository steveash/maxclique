package com.github.steveash.maxclique;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;

/**
 * For a given graph returns the maximum clique, then the maximum clique from the remaining verticies,
 * then the next, etc.  Eventually all of the individual verticies will be returned as individual cliques
 * if they don't fit in any larger cliques
 * @author Steve Ash
 */
public class AllMaxCliqueFinder<T> implements Iterable<Clique<T>> {

    private final Iterable<T> initialVerticies;
    private final MaxCliqueFinder<T> finder;
    private final Weigher<T> weigher;

    public AllMaxCliqueFinder(Iterable<T> initialVerticies, MaxCliqueFinder<T> finder, Weigher<T> weigher) {
        this.initialVerticies = initialVerticies;
        this.finder = finder;
        this.weigher = weigher;
    }

    @Override
    public Iterator<Clique<T>> iterator() {

        final List<T> remaining = Lists.newArrayList(initialVerticies);

        return new AbstractIterator<Clique<T>>() {

            @Override
            protected Clique<T> computeNext() {
                if (remaining.isEmpty()) {
                    return endOfData();
                }
                Clique<T> nextMaximum = finder.findMaximum(remaining, weigher);

                markOutRemaining(nextMaximum);
                return nextMaximum;
            }

            // need the order preserved; for my use case graphs are small so copy is probably faster
            private void markOutRemaining(Clique<T> nextMaximum) {
                remaining.removeAll(nextMaximum.members());
            }
        };
    }
}
