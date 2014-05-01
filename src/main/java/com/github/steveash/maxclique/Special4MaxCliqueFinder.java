package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Special case max clique finder for |V| == 4
 * @author Steve Ash
 */
public class Special4MaxCliqueFinder<T> implements MaxCliqueFinder<T> {
    private static final boolean O = true;
    private static final boolean F = false;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        assert(elements.size() == 4);

        Iterator<T> iter = elements.iterator();
        T a = iter.next();
        T b = iter.next();
        T c = iter.next();
        T d = iter.next();

        double bestWeight = 0;
        int bestMask = 1 << 3;

        // 2 cliques
        double wab = weigh(weigher, a, b);
        if (wab > bestWeight) { bestWeight = wab; bestMask = mask(O, O, F, F); }

        double wac = weigh(weigher, a, c);
        if (wac > bestWeight) { bestWeight = wac; bestMask = mask(O, F, O, F); }

        double wad = weigh(weigher, a, d);
        if (wad > bestWeight) { bestWeight = wad; bestMask = mask(O, F, F, O); }

        double wbc = weigh(weigher, b, c);
        if (wbc > bestWeight) { bestWeight = wbc; bestMask = mask(F, O, O, F); }

        double wbd = weigh(weigher, b, d);
        if (wbd > bestWeight) { bestWeight = wbd; bestMask = mask(F, O, F, O); }

        double wcd = weigh(weigher, c, d);
        if (wcd > bestWeight) { bestWeight = wcd; bestMask = mask(F, F, O, O); }

        // 3 cliques
        double wabc = wab + wac + wbc;
        if (wabc > bestWeight) { bestWeight = wabc; bestMask = mask(O, O, O, F); }

        double wabd = wab + wad + wbd;
        if (wabd > bestWeight) { bestWeight = wabd; bestMask = mask(O, O, F, O); }

        double wacd = wac + wad + wcd;
        if (wacd > bestWeight) { bestWeight = wacd; bestMask = mask(O, F, O, O); }

        double wbcd = wbc + wbd + wcd;
        if (wbcd > bestWeight) { bestWeight = wbcd; bestMask = mask(F, O, O, O); }

        // 4 cliques
        double wabcd = wab + wac + wad + wbc + wbd + wcd;
        if (wabcd > bestWeight) { bestWeight = wabcd; bestMask = mask(O, O, O, O); }

        Builder<T> builder = ImmutableSet.builder();
        if ((bestMask & (1 << 3)) > 0) builder.add(a);
        if ((bestMask & (1 << 2)) > 0) builder.add(b);
        if ((bestMask & (1 << 1)) > 0) builder.add(c);
        if ((bestMask & (1 << 0)) > 0) builder.add(d);

        return new Clique<>(builder.build(), bestWeight);
    }

    private double weigh(Weigher<T> weigher, T a, T b) {
        double w = weigher.weigh(a, b);
        if (w <= 0) return Double.NEGATIVE_INFINITY;
        return w;
    }

    private int mask(boolean a, boolean b, boolean c, boolean d) {
        int mask = 0;
        if (a) mask |= 1 << 3;
        if (b) mask |= 1 << 2;
        if (c) mask |= 1 << 1;
        if (d) mask |= 1 << 0;
        return mask;
    }

    private boolean greatest(double out, double in1, double in2, double in3) {
        if (out < 0) return false;
        return (out >= in1 && out >= in2 && out >= in3);
    }

    private Clique<T> c2(T a, T b, double totalWeight) {
        return new Clique<>(ImmutableSet.of(a, b), totalWeight);
    }
}
