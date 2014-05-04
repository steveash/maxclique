package com.github.steveash.maxclique

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

/**
 * @author Steve Ash
 */
@RunWith(Parameterized.class)
public class Special3FinderTest extends BaseCliqueFinderTest {

    public Special3FinderTest(MaxCliqueFinder finder) {
        super(finder);
    }

    @Parameters
    public static Collection params() {
        return [
                [new Special3Finder<String>()] as Object[],
                [new GeneralFinder<String>()] as Object[]
        ]
    }

    @Test
    public void shouldFind1() throws Exception {
        assert findClique(3).members() == ["A"].toSet()
    }

    @Test
    public void shouldFind2() throws Exception {
        w.clear().put("A", "B", 10)
        assert findClique(3, true).members() == ["A", "B"].toSet()

        // add superfluous noise
        w.put("B", "C", 5)
        assert findClique(3, true).members() == ["A", "B"].toSet()
    }

    @Test
    public void shouldFind3() throws Exception {
        w.clear().put("A", "B", 10)
                .put("A", "C", 10)
                .put("B", "C", 10)

        assert findClique(3, true).members() == ["A", "B", "C"].toSet()
    }
}
