package com.github.steveash.maxclique;

/**
 * Symmetric matrix for weights stored in a 1-dim vector of doubles;
 * diagonal entries always return zero
 * @author Steve Ash
 */
public class WeightMatrix {

    private final int dimension;
    private final double[] weights;

    public WeightMatrix(int dimension) {
        this.dimension = dimension;

        int entries = dimension * (dimension - 1) / 2;
        this.weights = new double[entries];
    }

    public double weight(int r, int c) {
        if (r == c) return 0;
        return weights[index(c, r)];
    }

    public void set(int r, int c, double value) {
        if (r == c) return;
        weights[index(r, c)] = value;
    }

    private int index(int r, int c) {
        // precondition the r != c
        if (r > c) return index(c, r);
        return r * (dimension - 1) - (r - 1) * ((r - 1) + 1) / 2 + c - r - 1;
    }

    protected double[] getWeights() {
        return weights;
    }
}
