package com.github.steveash.maxclique

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

/**
 * @author Steve Ash
 */
@RunWith(Parameterized.class)
public class Special2FinderTest extends BaseCliqueFinderTest {

    public static final int NODE_COUNT = 2

    public Special2FinderTest(MaxCliqueFinder finder) {
        super(finder);
    }

    @Parameters
    public static Collection params() {
        return [
                [new Special2MaxCliqueFinder<String>()] as Object[],
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
    }
}
