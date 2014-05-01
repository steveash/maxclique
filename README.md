maxclique
=========

Simple implementation of maximum clique for java

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
