package com.github.steveash.maxclique

import groovy.transform.CompileStatic

/**
 * @author Steve Ash
 */
@CompileStatic
class Bron1CompareTest extends BaseCompareTest {

    Bron1CompareTest() {
        super(
                new NaiveFinder<String>(),
                new BronKerbosch1Finder<String>()
        )
    }
}
