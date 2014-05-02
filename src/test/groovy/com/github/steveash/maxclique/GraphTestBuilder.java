package com.github.steveash.maxclique;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Steve Ash
 */
public class GraphTestBuilder {
    private static final Random rand = new Random(123456);

    public static class Graph {
        public final List<String> verticies;
        public final TableWeigher edges;

        private Graph(List<String> verticies, TableWeigher edges) {
            this.verticies = verticies;
            this.edges = edges;
        }
    }

    public static class Edge {
        public final String a;
        public final String b;

        private Edge(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public List<Graph> buildInput(int graphSize, int count) {
        List<String> verticies = Lists.newArrayListWithCapacity(graphSize);
        for (int i = 0; i < graphSize; i++) {
            verticies.add(Integer.toString(i));
        }

        List<Edge> pairs = makePairs(graphSize);
        List<Graph> graphs = Lists.newArrayListWithCapacity(count);
        for (int i = 0; i < count; i++) {

            int maxEdges = graphSize * (graphSize - 1) / 2;
            int thisEdges = rand.nextInt(maxEdges) + 1;
            Collections.shuffle(pairs, rand);

            TableWeigher w = new TableWeigher();
            for (int j = 0; j < thisEdges; j++) {
                Edge edge = pairs.get(j);
                double ww = (rand.nextDouble() * 10) + 10;
                w.put(edge.a, edge.b, ww);
            }
            graphs.add(new Graph(verticies, w));
        }
        return graphs;
    }

    private List<Edge> makePairs(int graphSize) {
        int pairCount = graphSize * (graphSize - 1) / 2;
        List<Edge> nodes = Lists.newArrayListWithCapacity(pairCount);
        for (int i = 0; i < graphSize; i++) {
            for (int j = i + 1; j < graphSize; j++) {
                nodes.add(new Edge(Integer.toString(i), Integer.toString(j)));
            }
        }
        Preconditions.checkState(nodes.size() == pairCount);
        return nodes;
    }
}
