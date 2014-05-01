package com.github.steveash.maxclique

import org.junit.Before

/**
 * @author Steve Ash
 */
class BaseCliqueFinderTest {

    TableWeigher w
    private final MaxCliqueFinder finder

    public BaseCliqueFinderTest(MaxCliqueFinder finder) {
        this.finder = finder
    }

    @Before
    public void setUp() throws Exception {
        this.w = new TableWeigher()
    }

    protected Clique findClique(int nodeCount, boolean checkAllPermutations = false) {
        def nodes = ("A".."Z").take(nodeCount)

        if (!checkAllPermutations) {
            return finder.findMaximum(nodes, w)
        }

        def c = null
        for (def input : nodes.permutations()) {
            def cc = finder.findMaximum(input, w)
            if (c == null) c = cc;
            else {
                assert c == cc
                assert Math.abs(c.totalWeight() - cc.totalWeight()) < 0.001
            }
        }
        return c
    }
}
