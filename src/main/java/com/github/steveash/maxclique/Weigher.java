package com.github.steveash.maxclique;

/**
 * Stategy that knows how to weigh two elements of a graph - i.e. returns the edge weight for two verticies
 * Weights are all positive: w(_,_) > 0; returning a value <= 0 means returning negative infinity; that edge cant be in a clique
 * Weights are symmetric: w(a,b) = w(b,a)
 * @author Steve Ash
 */
public interface Weigher<T> {

    /**
     * Verticies in the graph
     * @param a
     * @param b
     * @return positive weight of the edge between a and b; if value <= 0 then it is treated as "negative infinity"
     * meaning that this edge cannot be in a clique
     */
    double weigh(T a, T b);
}
