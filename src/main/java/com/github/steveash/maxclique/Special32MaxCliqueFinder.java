package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.IntSet;
import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;

/**
 * Special version of the clique finder for graphs with <= 64 verticies
 * @see com.github.steveash.maxclique.GeneralMaxCliqueFinder
 * @author Steve Ash
 */
public class Special32MaxCliqueFinder<T> implements MaxCliqueFinder<T> {
    private static final Logger log = LoggerFactory.getLogger(Special32MaxCliqueFinder.class);

    private IntSet seenCliques;                // keep track of what cliques we've already seen
    private int lastWorkedSize;                 // process partials in increasing order to "forget" seen early
    private Deque<MaskPartialClique> work;       // work queue

    private MaskPartialClique best = MaskPartialClique.nullInstance;

    private Graph<T> g;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        Preconditions.checkArgument(elements.size() <= 32, " can only use this for sets <= 64");

        init(elements, weigher);
        if (g.size() == 0) return Clique.nullClique();

        while (!work.isEmpty()) {
            MaskPartialClique clique = work.removeFirst();

            int thisSize = clique.size();
            assert (thisSize >= lastWorkedSize);
            if (thisSize > lastWorkedSize) {
                assert(thisSize == lastWorkedSize + 1);

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

    private void tryToAdd(MaskPartialClique clique, int toCheck) {
        int newMembers = clique.copyOfMembersPlus(toCheck);
        if (seenCliques.contains(newMembers)) {
            return; // don't even bother -- someone else is already working on it
        }

        double maybe = clique.candidateJoinWeight(toCheck, g);
        if (maybe <= 0) {
            return; // not accepted
        }
        double totalNewWeight = clique.getMemberWeight() + maybe;

        // we can accept!
        int leftToCheck = g.neighborsExcludingAsMask(toCheck, newMembers);
        addNewClique(newMembers, leftToCheck, totalNewWeight);
    }

    private Clique<T> convertBest() {
        return best.convertToClique(g);
    }

    private void finishClique(MaskPartialClique clique) {
        if (clique.getMemberWeight() > best.getMemberWeight()) {
            best = clique;
        }
    }

    private void init(Iterable<T> elements, Weigher<T> weigher) {
        g = new Graph<>(elements, weigher);
        seenCliques = IntOpenHashSet.newInstanceWithExpectedSize(g.size() * (g.size() - 1) / 2);
        work = Queues.newArrayDeque();      // queue of partials to evaluate

        this.best = MaskPartialClique.nullInstance;
        lastWorkedSize = 0;

        if (log.isDebugEnabled()) {
            log.debug("Starting search for clique on graph size " + g.size());
        }

        for (int i = 0; i < g.size(); i++) {
            int member = (1 << i);

            int neighbors = g.neighborsExcludingAsMask(i, 0);
            addNewClique(member, neighbors, 0);
        }
    }

    private void addNewClique(int members, int leftToCheck, double weight) {
        work.addLast(new MaskPartialClique(members, leftToCheck, weight));
    }
}
