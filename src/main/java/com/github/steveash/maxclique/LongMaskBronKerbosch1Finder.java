package com.github.steveash.maxclique;

import static com.github.steveash.maxclique.SetUtils.*;

import java.util.Collection;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Simplest BronKerbosch implementation to find the maximum edge weighted clique.  Uses recursion
 * so will probably explode with really large dense graphs
 * @author Steve Ash
 */
public class LongMaskBronKerbosch1Finder<T> implements MaxCliqueFinder<T> {
//    private static final Logger log = LoggerFactory.getLogger(BronKerbosch1Finder.class);

    private long bestMask;
    private double bestWeight;
    private Graph<T> graph;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        Preconditions.checkArgument(elements.size() > 0, "must call with non empty graph");
        init(elements, weigher);

        long start = graph.allVerticiesAsMask();
        find(0L, 0, start, 0L);

        return bestToClique(graph);
    }

    private void find(final long candidate, double candidateWeight, long toCheck, long toExclude) {
//        log.info("Checking {} using toCheck {} and toExclude {}", candidate, toCheck, toExclude);
        if (isMaskEmpty(toCheck) && isMaskEmpty(toExclude)) {
//            log.info("Found maximal clique {}", candidate);
            if (candidateWeight > bestWeight) {
                this.bestWeight = candidateWeight;
                this.bestMask = candidate;
            }
            return;
        }

        while (!isMaskEmpty(toCheck)) {

            int v = SetUtils.nextToCheck(toCheck);
            long vNeighbors = graph.neighborsAsMask(v);
            // can accept v as a maximal clique only if all current candidate nodes are neighbors of v
            if (!isSubsetOf(candidate, vNeighbors)) {
                continue;
            }
            double newWeight = candidateWeight + newEdgeWeightAdding(candidate, v);
            long recurseCandidate = addToMask(candidate, v);
            long recurseToCheck = intersection(toCheck, vNeighbors);
            long recurseToExclude = intersection(toExclude, vNeighbors);
            find(recurseCandidate, newWeight, recurseToCheck, recurseToExclude);

            toCheck = popNextToCheck(toCheck);
            toExclude = addToMask(toExclude, v);
        }
    }

    private double newEdgeWeightAdding(long existingVs, int vToAdd) {
        double newEdgesWeight = 0;
        while (!isMaskEmpty(existingVs)) {

            int nextToCheck = nextToCheck(existingVs);
            newEdgesWeight += graph.weight(nextToCheck, vToAdd);
            existingVs = popNextToCheck(existingVs);
        }
        return newEdgesWeight;
    }

    private void init(Collection<T> elements, Weigher<T> weigher) {
        graph = new Graph<>(elements, weigher);
        this.bestMask = 1;
        this.bestWeight = 0;
//        log.info("Finding for {}", graph);
    }

    public <T> Clique<T> bestToClique(Graph<T> g) {
        Builder<T> b = ImmutableSet.builder();
        for (int i = 0; i < g.size(); i++) {
            long value = (1 << i);
            if ((this.bestMask & value) != 0) {
                b.add(g.vertexAt(i));
            }
        }
        return new Clique<>(b.build(), bestWeight);
    }
}
