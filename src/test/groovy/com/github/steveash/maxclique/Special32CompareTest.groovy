package com.github.steveash.maxclique

import groovy.transform.CompileStatic

/**
 * @author Steve Ash
 */
@CompileStatic
class Special32CompareTest extends BaseCompareTest {

    Special32CompareTest() {
        super(
                new NaiveFinder<String>(),
                new LongMaskBronKerbosch1Finder<String>()
        )
    }
}
