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

    private static final int GRAPH_COUNT = 2000;
    private static final int GRAPHSET_COUNT = 100;
    private GraphTestBuilder builder = new GraphTestBuilder();

    @Test
    public void shouldTestVariousSizes() throws Exception {
        Stopwatch w = Stopwatch.createUnstarted();
        MaxCliqueFinderFacade<String> finder = new MaxCliqueFinderFacade<>();

        int graphCount = 500;
        int runCount = 25;
        for (int i = 3; i < 20; i++) {
            List<Graph> in = builder.buildInput(i, graphCount);
            run(in, finder, runCount);

            w.reset().start();
            run(in, finder, runCount);
            long micros = w.stop().elapsed(TimeUnit.MICROSECONDS);
            long count = graphCount * runCount;

            double avg = ((double) micros) / count;
            log.info("Graph Size " + i + " total " + micros + " avg mus per " + avg);
        }
    }

    @Test
    public void shouldBeFasterWithFastPath() throws Exception {
        MaxCliqueFinder<String> gf = new LongMaskBronKerbosch1Finder<>();
        MaxCliqueFinder<String> sf = new Special4Finder<>();

        compareSpeedOf(gf, sf, 4);
    }

    @Test
    public void bronShouldBeFaster() throws Exception {
        MaxCliqueFinder<String> gf = new LongMaskBronKerbosch1Finder<>();
        MaxCliqueFinder<String> sf = new BronKerbosch1Finder<>();
        compareSpeedOf(gf, sf, 8);
    }

    private void compareSpeedOf(MaxCliqueFinder<String> afinder, MaxCliqueFinder<String> bfinder, int graphSize) {
        Stopwatch w = Stopwatch.createUnstarted();
        List<Graph> graphs = builder.buildInput(graphSize, GRAPH_COUNT);

        double total = 0;
        total += run(graphs, afinder, GRAPHSET_COUNT);
        total += run(graphs, bfinder, GRAPHSET_COUNT);

        w.start();
        total += run(graphs, afinder, GRAPHSET_COUNT);
        long atime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        w.reset().start();
        total += run(graphs, bfinder, GRAPHSET_COUNT);
        long btime = w.stop().elapsed(TimeUnit.MILLISECONDS);

        log.info("Overall total weight: " + total);
        log.info(afinder.getClass().getSimpleName() + " mus " + atime);
        log.info(bfinder.getClass().getSimpleName() + " mus " + btime);
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
