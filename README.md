maxclique
=========

- Simple implementation of maximum edge weighted clique for java using the Bron-Kerbosch algorithm.
- Doesn't recompute weight edges more than once.
- Has "fast path" hardcoded implementations for graphs with 2, 3, 4, and 5 nodes (which is my typical case).
- For graphs size 6 - 64 it uses long to represent sets avoiding extra allocations.
- Can find max weighted clique in < 15 microsecs for graphs <= 12 nodes.  Less than 1 microsec for graphs <= 5 nodes

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
