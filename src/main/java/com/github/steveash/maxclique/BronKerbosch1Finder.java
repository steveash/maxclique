package com.github.steveash.maxclique;

import static com.github.steveash.maxclique.SetUtils.emptySet;
import static com.github.steveash.maxclique.SetUtils.singletonSet;

import java.util.Collection;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.base.Preconditions;

/**
 * Simplest BronKerbosch implementation to find the maximum edge weighted clique.  Uses recursion
 * so will probably explode with really large dense graphs
 * @author Steve Ash
 */
public class BronKerbosch1Finder<T> implements MaxCliqueFinder<T> {

    private BronPartial best;
    private Graph<T> graph;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        Preconditions.checkArgument(elements.size() > 0, "must call with non empty graph");
        init(elements, weigher);

        IntOpenHashSet start = graph.copyOfAllVerticies();
        find(BronPartial.nullPartial, start, emptySet());

        return best.convertToClique(graph);
    }

    private void find(BronPartial candidate, IntOpenHashSet toCheck, IntOpenHashSet toExclude) {
        if (toCheck.isEmpty() && toExclude.isEmpty()) {
            if (candidate.getWeight() > best.getWeight()) {
                best = candidate;
            }
            return;
        }

        IntOpenHashSet newToCheck = SetUtils.copy(toCheck);
        for (IntCursor cursor : toCheck) {
            int v = cursor.value;
            BronPartial maybePartial = candidate.tryWith(v, graph);
            if (maybePartial == null) continue;

            IntOpenHashSet recurseToCheck = graph.intersectionWithNeighbors(newToCheck, v);
            IntOpenHashSet recurseToExclude = graph.intersectionWithNeighbors(toExclude, v);
            find(maybePartial, recurseToCheck, recurseToExclude);

            newToCheck.remove(v);
            toExclude.add(v);
        }
    }

    private void init(Collection<T> elements, Weigher<T> weigher) {
        graph = new Graph<>(elements, weigher);
        this.best = new BronPartial(singletonSet(0), 0);
    }
}
