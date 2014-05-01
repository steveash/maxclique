package com.github.steveash.maxclique

import org.junit.Test

/**
 * @author Steve Ash
 */
class GeneralMaxCliqueFinderTest extends BaseCliqueFinderTest {

    GeneralMaxCliqueFinderTest() {
        super(new GeneralMaxCliqueFinder())
    }

    @Test
    public void shouldFindSimpleCases() throws Exception {
        def c = findClique(6)
        assert c.members().size() == 1
        assert c.members().first() == "A"

        ("A".."F").toList().permutations()
        w.clear().put("A", "B", 10)
        c = findClique(6, true)

        assert c.members().size() == 2
        assert c.members() == ["A", "B"].toSet()
    }

    @Test
    public void shouldFindBiggerOnes() throws Exception {
        w.clear()
        .put("A", "B", 10)
        .put("A", "C", 10)
        .put("B", "C", 10)
        .put("C", "D", 20)
        .put("D", "E", 20)

        def c = findClique(6, true)
        assert c.members() == ["A", "B", "C"].toSet()

        // add an edge to make the clique switch
        w.put("C", "E", 20)
        c = findClique(6, true)
        assert c.members() == ["C", "D", "E"].toSet()

        // add another edge but it doesn't change cliques
        w.put("B", "D", 20)
        c = findClique(6, true)
        assert c.members() == ["C", "D", "E"].toSet()

        // add the last edge in a bigger clique
        w.put("B", "E", 20)
        assert findClique(6, true).members() == ("B".."E").toSet()
    }
}
