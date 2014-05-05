package com.github.steveash.maxclique

import groovy.transform.CompileStatic
import org.junit.Test

/**
 * @author Steve Ash
 */
@CompileStatic
abstract class BaseCompareTest {
    private final GraphTestBuilder builder = new GraphTestBuilder();

    private final MaxCliqueFinder<String> afinder;
    private final MaxCliqueFinder<String> bfinder;

    BaseCompareTest(MaxCliqueFinder<String> afinder, MaxCliqueFinder<String> bfinder) {
        this.afinder = afinder
        this.bfinder = bfinder
    }

    @Test
    public void shouldCompare3GraphsAndReturnTheSameAnswer() throws Exception {
        compareKGraphs(3, 500)
    }

    @Test
    public void shouldCompare4GraphsAndReturnTheSameAnswer() throws Exception {
        compareKGraphs(3, 500)
    }

    @Test
    public void shouldCompare5GraphsAndReturnTheSameAnswer() throws Exception {
        compareKGraphs(3, 500)
    }

    @Test
    public void shouldCompareABunchAndReturnTheSameAnswer() {

        for (int i = 3; i < 13; i++) {
            compareKGraphs(i, 4)
            println "Finished trying size $i"
        }
    }

    private void compareKGraphs(int graphSize, int graphCount) {
        def graphs = builder.buildInput(graphSize, graphCount)
        for (def g : graphs) {
            compare2(g)
        }
    }

    private void compare2(GraphTestBuilder.Graph testGraph) {
        def ac = afinder.findMaximum(testGraph.verticies, testGraph.edges)
        def bc = bfinder.findMaximum(testGraph.verticies, testGraph.edges)

        assert ac.members() == bc.members()
        assert Math.abs(ac.totalWeight() - bc.totalWeight()) < 0.001
    }
}
