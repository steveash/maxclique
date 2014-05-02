maxclique
=========

Simple implementation of maximum clique for java.
Doesn't recompute weight edges more than once.
Uses simple back-tracking to avoid all 2^n comparisons.
Has "fast path" hardcoded implementations for graphs with 2, 3, 4, and 5 nodes (which is my typical case).
For graphs size 6 - 64 it uses longs to represent sets which avoids lots of allocations

Usage:
```` java
List<T> nodesInMyGraph = ...

// need to make an insteance of Weigher<T> which knows how to produce a "weight" (double)
// given two nodes (T's) from the graph; any weights <= 0 are treated as negative infinity
Weigher<T> weigher = ...

// find the maximum clique in the graph
Clique<T> maximumClique = Cliques.findMaximum(nodesInMyGraph, weigher);

// the members of the clique will be in the set: maximumClique.members()
````
