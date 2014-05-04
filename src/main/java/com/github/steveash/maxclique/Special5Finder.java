package com.github.steveash.maxclique;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;

/**
 * Special case max clique finder for |V| == 5
 * @author Steve Ash
 */
public class Special5Finder<T> implements MaxCliqueFinder<T> {
    private static final boolean O = true;
    private static final boolean F = false;

    @Override
    public Clique<T> findMaximum(Collection<T> elements, Weigher<T> weigher) {
        assert(elements.size() == 5);

        Iterator<T> iter = elements.iterator();
        T a = iter.next();
        T b = iter.next();
        T c = iter.next();
        T d = iter.next();
        T e = iter.next();

        double bestWeight = 0;
        int bestMask = 1 << 4;

        // 2 cliques
        double wab = weigh(weigher, a, b);
        if (wab > bestWeight) { bestWeight = wab; bestMask = mask(O, O, F, F, F); }

        double wac = weigh(weigher, a, c);
        if (wac > bestWeight) { bestWeight = wac; bestMask = mask(O, F, O, F, F); }

        double wad = weigh(weigher, a, d);
        if (wad > bestWeight) { bestWeight = wad; bestMask = mask(O, F, F, O, F); }

        double wae = weigh(weigher, a, e);
        if (wae > bestWeight) { bestWeight = wae; bestMask = mask(O, F, F, F, O); }

        double wbc = weigh(weigher, b, c);
        if (wbc > bestWeight) { bestWeight = wbc; bestMask = mask(F, O, O, F, F); }

        double wbd = weigh(weigher, b, d);
        if (wbd > bestWeight) { bestWeight = wbd; bestMask = mask(F, O, F, O, F); }

        double wbe = weigh(weigher, b, e);
        if (wbe > bestWeight) { bestWeight = wbe; bestMask = mask(F, O, F, F, O); }

        double wcd = weigh(weigher, c, d);
        if (wcd > bestWeight) { bestWeight = wcd; bestMask = mask(F, F, O, O, F); }

        double wce = weigh(weigher, c, e);
        if (wce > bestWeight) { bestWeight = wce; bestMask = mask(F, F, O, F, O); }

        double wde = weigh(weigher, d, e);
        if (wde > bestWeight) { bestWeight = wde; bestMask = mask(F, F, F, O, O); }

        // 3 cliques
        double wabc = wab + wac + wbc;
        if (wabc > bestWeight) { bestWeight = wabc; bestMask = mask(O, O, O, F, F); }

        double wabd = wab + wad + wbd;
        if (wabd > bestWeight) { bestWeight = wabd; bestMask = mask(O, O, F, O, F); }

        double wabe = wab + wae + wbe;
        if (wabe > bestWeight) { bestWeight = wabe; bestMask = mask(O, O, F, F, O); }

        double wacd = wac + wad + wcd;
        if (wacd > bestWeight) { bestWeight = wacd; bestMask = mask(O, F, O, O, F); }

        double wace = wac + wae + wce;
        if (wace > bestWeight) { bestWeight = wace; bestMask = mask(O, F, O, F, O); }

        double wade = wad + wae + wde;
        if (wade > bestWeight) { bestWeight = wade; bestMask = mask(O, F, F, O, O); }

        double wbcd = wbc + wbd + wcd;
        if (wbcd > bestWeight) { bestWeight = wbcd; bestMask = mask(F, O, O, O, F); }

        double wbce = wbc + wbe + wce;
        if (wbce > bestWeight) { bestWeight = wbce; bestMask = mask(F, O, O, F, O); }

        double wbde = wbd + wbe + wde;
        if (wbde > bestWeight) { bestWeight = wbde; bestMask = mask(F, O, F, O, O); }

        double wcde = wcd + wce + wde;
        if (wcde > bestWeight) { bestWeight = wcde; bestMask = mask(F, F, O, O, O); }

        // 4 cliques
        double wabcd = wab + wac + wad + wbc + wbd + wcd;
        if (wabcd > bestWeight) { bestWeight = wabcd; bestMask = mask(O, O, O, O, F); }

        double wabce = wab + wac + wae + wbc + wbe + wce;
        if (wabce > bestWeight) { bestWeight = wabce; bestMask = mask(O, O, O, F, O); }

        double wabde = wab + wad + wae + wbd + wbe + wde;
        if (wabde > bestWeight) { bestWeight = wabde; bestMask = mask(O, O, F, O, O); }

        double wacde = wac + wad + wae + wcd + wce + wde;
        if (wacde > bestWeight) { bestWeight = wacde; bestMask = mask(O, F, O, O, O); }

        double wbcde = wbc + wbd + wbe + wcd + wce + wde;
        if (wbcde > bestWeight) { bestWeight = wbcde; bestMask = mask(F, O, O, O, O); }

        // 5 cliques
        double wabcde = wab + wac + wad + wae + wbc + wbd + wbe + wcd + wce + wde;
        if (wabcde > bestWeight) { bestWeight = wabcde; bestMask = mask(O, O, O, O, O); }

        Builder<T> builder = ImmutableSet.builder();
        if ((bestMask & (1 << 4)) > 0) builder.add(a);
        if ((bestMask & (1 << 3)) > 0) builder.add(b);
        if ((bestMask & (1 << 2)) > 0) builder.add(c);
        if ((bestMask & (1 << 1)) > 0) builder.add(d);
        if ((bestMask & (1 << 0)) > 0) builder.add(e);

        return new Clique<>(builder.build(), bestWeight);
    }

    private double weigh(Weigher<T> weigher, T a, T b) {
        double w = weigher.weigh(a, b);
        if (w <= 0) return Double.NEGATIVE_INFINITY;
        return w;
    }

    private int mask(boolean a, boolean b, boolean c, boolean d, boolean e) {
        int mask = 0;
        if (a) mask |= 1 << 4;
        if (b) mask |= 1 << 3;
        if (c) mask |= 1 << 2;
        if (d) mask |= 1 << 1;
        if (e) mask |= 1 << 0;
        return mask;
    }
}
