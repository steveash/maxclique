package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Deque;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntDeque;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;
import com.carrotsearch.hppc.cursors.IntCursor;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

/**
 * Finder that can take an input of elements and a way to weigh edges between elements and returns the highest weight
 * (maximum) clique from the set
 * @author Steve Ash
 */
public class GeneralMaxCliqueFinder<T> implements MaxCliqueFinder<T> {
    private static final Logger log = LoggerFactory.getLogger(GeneralMaxCliqueFinder.class);
    private static final IntOpenHashSet emptySet = IntOpenHashSet.from();

    private Set<IntSet> seenCliques;            // keep track of what cliques we've already seen
    private int lastWorkedSize;                 // process partials in increasing order to "forget" seen early
    private Deque<PartialClique<T>> work;       // work queue

    private double bestWeight = -1;             // once we finish a partial we keep track of the best
    private IntSet bestClique = null;

    private Graph<T> g;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        init(elements, weigher);
        if (g.size() == 0) return Clique.nullClique();

        while (!work.isEmpty()) {
            PartialClique<T> clique = work.removeFirst();
            logPop(clique);

            int thisSize = clique.size();
            assert (thisSize >= lastWorkedSize) : "this size "  + thisSize + ", lastWorked " + lastWorkedSize;
            if (thisSize > lastWorkedSize) {
                assert(thisSize == lastWorkedSize + 1);
                assert(!anySeenGtEqTo(thisSize + 1));

                seenCliques.clear();
                lastWorkedSize = thisSize;
            }

            while (clique.hasMoreToCheck()) {
                int toCheck = clique.popNextToCheck();
                tryToAdd(clique, toCheck);
            }
            finishClique(clique);
        }
        return convertBest();
    }

    private void logPop(PartialClique<T> clique) {
        if (log.isDebugEnabled()) {
            log.debug("Working partial size " + clique.size() + " " + clique.getMembers());
        }
    }

    private boolean anySeenGtEqTo(int sizeShouldntBeSeen) {
        for (IntSet seen : seenCliques) {
            if (seen.size() >= sizeShouldntBeSeen) {
                return true;
            }
        }
        return false;
    }

    private void tryToAdd(PartialClique<T> clique, int toCheck) {
        IntSet newMembers = clique.copyOfMembersPlus(toCheck);
        if (seenCliques.contains(newMembers)) {
            return; // don't even bother -- someone else is already working on it
        }

        double totalNewWeight = clique.getMemberWeight();
        for (IntCursor existing : clique.getMembers()) {
            double edge = g.weight(existing.value, toCheck);
            if (edge <= 0) {
                return; // edge not accepted, not a maximal clique
            }
            totalNewWeight += edge;
        }

        // we can accept!
        IntDeque leftToCheck = g.neighborsExcluding(toCheck, newMembers);
        addNewClique(newMembers, leftToCheck, totalNewWeight);
    }

    private Clique<T> convertBest() {
        Preconditions.checkState(bestWeight >= 0);
        return new Clique<>(g.verticiesForIndexes(bestClique), bestWeight);
    }

    private void finishClique(PartialClique<T> clique) {
        if (clique.getMemberWeight() > bestWeight) {
            bestWeight = clique.getMemberWeight();
            bestClique = clique.getMembers();
        }
    }

    private void init(Iterable<T> elements, Weigher<T> weigher) {
        g = new Graph<>(elements, weigher);
        seenCliques = Sets.newHashSet();    // don't follow the same path twice
        work = Queues.newArrayDeque();      // queue of partials to evaluate

        bestWeight = -1;
        bestClique = null;
        lastWorkedSize = 0;

        if (log.isDebugEnabled()) {
            log.debug("Starting search for clique on graph size " + g.size());
        }

        for (int i = 0; i < g.size(); i++) {
            IntSet single = new IntOpenHashSet(1);
            single.add(i);

            IntDeque neighbors = g.neighborsExcluding(i, emptySet);
            addNewClique(single, neighbors, 0);
        }
    }

    private void addNewClique(IntSet members, IntDeque leftToCheck, double weight) {
        if (log.isDebugEnabled()) {
            log.debug("Adding partial to eval size " + members.size() + " " + members);
        }
        work.addLast(new PartialClique<T>(members, leftToCheck, weight));
    }
}
