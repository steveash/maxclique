package com.github.steveash.maxclique

import groovy.transform.CompileStatic

/**
 * @author Steve Ash
 */
@CompileStatic
class MaskBron1CompareTest extends BaseCompareTest {

    MaskBron1CompareTest() {
        super(
                new LongMaskBronKerbosch1Finder<String>(),
                new BronKerbosch1Finder<String>()
        )
    }
}
