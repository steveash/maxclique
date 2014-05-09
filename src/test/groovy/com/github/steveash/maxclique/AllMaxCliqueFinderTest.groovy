package com.github.steveash.maxclique

/**
 * @author Steve Ash
 */
class AllMaxCliqueFinderTest extends GroovyTestCase {

    void testFindNone() {
        def g = ["0", "1", "2", "3", "4"]
        def w = new TableWeigher()

        def iter = Cliques.findAllMaximums(g, w).iterator()

        assert iter.next().members() == ["0"].toSet()
        assert iter.next().members() == ["1"].toSet()
        assert iter.next().members() == ["2"].toSet()
        assert iter.next().members() == ["3"].toSet()
        assert iter.next().members() == ["4"].toSet()
        assert !iter.hasNext()
    }

    void testFindAFew() {
        def g = ["0", "1", "2", "3", "4", "5"]
        def w = new TableWeigher()
        w.put("0", "1", 10.0)
        w.put("1", "2", 10.0)
        w.put("0", "2", 10.0)
        w.put("3", "4", 40.0)

        def iter = Cliques.findAllMaximums(g, w).iterator()
        assert iter.next().members() == ["3", "4"].toSet()
        assert iter.next().members() == ["0", "1", "2"].toSet()
        assert iter.next().members() == ["5"].toSet()
        assert !iter.hasNext()
    }

    void testFindOne() {
        def g = ["0", "1", "2", "3", "4", "5"]
        def w = new TableWeigher()
        w.put("0", "1", 10.0)
        w.put("1", "2", 10.0)
        w.put("0", "2", 10.0)

        def iter = Cliques.findAllMaximums(g, w).iterator()
        assert iter.next().members() == ["0", "1", "2"].toSet()
        assert iter.next().members() == ["3"].toSet()
        assert iter.next().members() == ["4"].toSet()
        assert iter.next().members() == ["5"].toSet()
        assert !iter.hasNext()
    }
}
