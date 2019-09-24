import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import com.leonis.android.rasalas.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by leonis on 2019/09/24.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testOnCreate() {
        assertEquals(true, true);
    }
}
