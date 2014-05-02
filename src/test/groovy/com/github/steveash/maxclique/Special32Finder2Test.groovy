package com.github.steveash.maxclique

import org.junit.Test

/**
 * @author Steve Ash
 */
class Special32Finder2Test {
    private final GraphTestBuilder builder = new GraphTestBuilder();

    @Test
    public void shouldFindABunch() {
        def sf = new Special32MaxCliqueFinder<>()
        def gf = new GeneralMaxCliqueFinder<>()

        for (int i = 3; i < 14; i++ ) {
            def inputs = builder.buildInput(i, 3)

            for (GraphTestBuilder.Graph testGraph : inputs) {

                def sa = sf.findMaximum(testGraph.verticies, testGraph.edges)
                def ga = gf.findMaximum(testGraph.verticies, testGraph.edges)

                assert sa.members() == ga.members()
                assert Math.abs(sa.totalWeight() - ga.totalWeight()) < 0.001
            }
            println "Finished trying size $i"
        }
    }
}
