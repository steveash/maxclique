package com.github.steveash.maxclique

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

/**
 * @author Steve Ash
 */
@RunWith(Parameterized.class)
public class Special4FinderTest extends BaseCliqueFinderTest {


    public static final int NODE_COUNT = 4

    public Special4FinderTest(MaxCliqueFinder finder) {
        super(finder);
    }

    @Parameters
    public static Collection params() {
        return [
                [new Special4MaxCliqueFinder<String>()] as Object[],
                [new GeneralMaxCliqueFinder<String>()] as Object[]
        ]
    }

    @Test
    public void shouldFind1() throws Exception {
        assert findClique(NODE_COUNT).members() == ["A"].toSet()
    }

    @Test
    public void shouldFind2() throws Exception {
        w.clear().put("A", "B", 10)
        assert findClique(NODE_COUNT, true).members() == ["A", "B"].toSet()

        // add superfluous noise
        w.put("B", "C", 5)
        assert findClique(NODE_COUNT, true).members() == ["A", "B"].toSet()
    }

    @Test
    public void shouldFind3() throws Exception {
        w.clear().put("A", "B", 10)
                .put("A", "C", 10)
                .put("B", "C", 10)

        assert findClique(NODE_COUNT, true).members() == ["A", "B", "C"].toSet()

        // noise
        w.put("C", "D", 5)
        assert findClique(NODE_COUNT, true).members() == ["A", "B", "C"].toSet()
    }

    @Test
    public void shouldFind4() throws Exception {
        w.clear().put("A", "B", 10)
                .put("A", "C", 10)
                .put("A", "D", 10)
                .put("B", "C", 10)
                .put("B", "D", 10)
                .put("C", "D", 10)

        assert findClique(NODE_COUNT, true).members() == ["A", "B", "C", "D"].toSet()
    }
}
