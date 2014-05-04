package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;

/**
 * Special case max clique finder for |V| == 3
 * @author Steve Ash
 */
public class Special3Finder<T> implements MaxCliqueFinder<T> {

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        assert(elements.size() == 3);

        Iterator<T> iter = elements.iterator();
        T a = iter.next();
        T b = iter.next();
        T c = iter.next();

        double wab = weigher.weigh(a, b);
        double wac = weigher.weigh(a, c);
        double wbc = weigher.weigh(b, c);

        double wabc;
        if (wab < 0 || wac < 0 || wbc < 0) {
            wabc = -1;
        } else {
            wabc = wab + wac + wbc;
        }

        if (greatest(wab, wac, wbc, wabc)) return c2(a, b, wab);
        if (greatest(wac, wab, wbc, wabc)) return c2(a, c, wac);
        if (greatest(wbc, wab, wac, wabc)) return c2(b, c, wbc);
        if (wabc > 0 && wabc >= wab && wabc >= wac && wabc >= wbc) {
            return new Clique<>(ImmutableSet.of(a, b, c), wabc);
        }
        // just return the first element since no clique greater size 1 exists
        return new Clique<>(ImmutableSet.of(a), 0);
    }

    private boolean greatest(double out, double in1, double in2, double in3) {
        if (out < 0) return false;
        return (out >= in1 && out >= in2 && out >= in3);
    }

    private Clique<T> c2(T a, T b, double totalWeight) {
        return new Clique<>(ImmutableSet.of(a, b), totalWeight);
    }
}
