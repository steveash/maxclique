package com.github.steveash.maxclique;

import java.util.List;
import java.util.Random;
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
            List<Graph> in = builder.buildInput(i, GRAPH_COUNT);
            run(in, finder);

            w.reset().start();
            run(in, finder);
            long micros = w.stop().elapsed(TimeUnit.MICROSECONDS);
            long count = GRAPH_COUNT * GRAPHSET_COUNT;

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
        total += run(graphs, gf);
        total += run(graphs, sf);

        w.start();
        total += run(graphs, gf);
        long genTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        w.reset().start();
        total += run(graphs, sf);
        long specTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        log.info("Overall total weight: " + total);
        log.info("General mus " + genTime + " , fastpath mus " + specTime);
    }

    @Test
    public void sholdBeFasterWithMasks() throws Exception {
        Stopwatch w = Stopwatch.createUnstarted();
        List<Graph> graphs = builder.buildInput(8, GRAPH_COUNT);
        MaxCliqueFinder<String> gf = new GeneralMaxCliqueFinder<>();
        MaxCliqueFinder<String> sf = new Special64MaxCliqueFinder<>();

        double total = 0;
        total += run(graphs, gf);
        total += run(graphs, sf);

        w.start();
        total += run(graphs, gf);
        long genTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        w.reset().start();
        total += run(graphs, sf);
        long specTime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        log.info("Overall total weight: " + total);
        log.info("General mus " + genTime + " , fastpath mus " + specTime);
    }

    private double run(List<Graph> graphs, MaxCliqueFinder<String> gf) {
        double total = 0;
        for (int i = 0; i < GRAPHSET_COUNT; i++) {
            for (int j = 0; j < graphs.size(); j++) {
                Graph graph = graphs.get(j);
                Clique<String> maximum = gf.findMaximum(graph.verticies, graph.edges);
                total += maximum.totalWeight();
            }
        }
        return total;
    }


}
