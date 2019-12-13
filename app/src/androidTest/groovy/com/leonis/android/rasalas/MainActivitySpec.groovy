package com.leonis.android.rasalas

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import static junit.framework.Assert.assertEquals

/**
 * Created by leonis on 2019/09/24.
 */

@RunWith(AndroidJUnit4.class)
class MainActivitySpec {
    def mainActivity

    @Rule
    public ActivityTestRule<MainActivity> mainActivityRule = new ActivityTestRule<>(MainActivity.class)

    @Before
    void setUp() throws Exception {
        mainActivity = mainActivityRule.getActivity()
    }

    @Test
    void testOnCreate() {
        assertEquals(true, true)
    }
}
