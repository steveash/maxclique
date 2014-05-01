package com.github.steveash.maxclique;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @author Steve Ash
 */
public class TableWeigher implements Weigher<String> {

    private final Table<String, String, Double> w = HashBasedTable.create();

    public TableWeigher clear() {
        w.clear();
        return this;
    }

    public TableWeigher put(String a, String b, double weight) {
        if (a.compareTo(b) > 0) {
            String t = a;
            a = b;
            b = t;
        }

        w.put(a, b, weight);
        return this;
    }

    @Override
    public double weigh(String a, String b) {
        if (a.compareTo(b) > 0) {
            String t = a;
            a = b;
            b = t;
        }

        Double maybe = w.get(a, b);
        if (maybe == null) return -1;

        return maybe;
    }
}
