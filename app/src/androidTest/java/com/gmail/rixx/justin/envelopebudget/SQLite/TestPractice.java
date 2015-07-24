package com.gmail.rixx.justin.envelopebudget.SQLite;

import android.test.AndroidTestCase;

/**
 * Created by justin on 7/24/15.
 */
public class TestPractice extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testTesting() throws Exception {
        int one = 1;
        int two = 2;
        int three = 3;
        int four = 4;

        // one plus one equals two
        assertEquals(one + one, two);

        // three times four equals twelve
        assertEquals(three * four, 10 + two);

        // fail
        // assertTrue(false);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
