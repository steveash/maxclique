package com.github.steveash.maxclique;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.steveash.maxclique.GraphTestBuilder.Graph;
import com.google.common.base.Stopwatch;

/**
 * @author Steve Ash
 */
@Ignore
public class PerfTest {
    private static final Logger log = LoggerFactory.getLogger(PerfTest.class);

    private static final int GRAPH_COUNT = 100;
    private static final int GRAPHSET_COUNT = 100;
    private GraphTestBuilder builder = new GraphTestBuilder();

    @Test
    public void shouldTestVariousSizes() throws Exception {
        Stopwatch w = Stopwatch.createUnstarted();
        MaxCliqueFinderFacade<String> finder = new MaxCliqueFinderFacade<>();

        for (int i = 3; i < 20; i++) {
            List<Graph> in = builder.buildInput(i, 1).subList(0, 1);
            run(in, finder, 1);

            w.reset().start();
            run(in, finder, 1);
            long micros = w.stop().elapsed(TimeUnit.MICROSECONDS);
            long count = 1;

            double avg = ((double) micros) / count;
            log.info("Graph Size " + i + " total " + micros + " avg mus per " + avg);
        }
    }

    @Test
    public void shouldBeFasterWithFastPath() throws Exception {
        Stopwatch w = Stopwatch.createUnstarted();
        List<Graph> graphs = builder.buildInput(4, GRAPH_COUNT);
        MaxCliqueFinder<String> gf = new GeneralMaxCliqueFinder<>();
        MaxCliqueFinder<String> sf = new Special4MaxCliqueFinder<>();

        double total = 0;
        total += run(graphs, gf, GRAPHSET_COUNT);
        total += run(graphs, sf, GRAPHSET_COUNT);

        w.start();
        total += run(graphs, gf, GRAPHSET_COUNT);
        long genTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        w.reset().start();
        total += run(graphs, sf, GRAPHSET_COUNT);
        long specTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        log.info("Overall total weight: " + total);
        log.info("General mus " + genTime + " , fastpath mus " + specTime);
    }

    @Test
    public void sholdBeFasterWithMasks() throws Exception {
        Stopwatch w = Stopwatch.createUnstarted();
        List<Graph> graphs = builder.buildInput(8, GRAPH_COUNT);
        MaxCliqueFinder<String> gf = new GeneralMaxCliqueFinder<>();
        MaxCliqueFinder<String> sf = new Special32MaxCliqueFinder<>();

        double total = 0;
        total += run(graphs, gf, GRAPHSET_COUNT);
        total += run(graphs, sf, GRAPHSET_COUNT);

        w.start();
        total += run(graphs, gf, GRAPHSET_COUNT);
        long genTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        w.reset().start();
        total += run(graphs, sf, GRAPHSET_COUNT);
        long specTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        log.info("Overall total weight: " + total);
        log.info("General mus " + genTime + " , fastpath mus " + specTime);
    }

    private double run(List<Graph> graphs, MaxCliqueFinder<String> gf, int runCount) {
        double total = 0;
        for (int i = 0; i < runCount; i++) {
            for (int j = 0; j < graphs.size(); j++) {
                Graph graph = graphs.get(j);
                Clique<String> maximum = gf.findMaximum(graph.verticies, graph.edges);
                total += maximum.totalWeight();
            }
        }
        return total;
    }


}
