package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by justin on 7/24/15.
 */
public class FullTestSuite extends TestSuite {

    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class).
                includeAllPackagesUnderHere().build();
    }

    public FullTestSuite() {
        super();
    }
}
