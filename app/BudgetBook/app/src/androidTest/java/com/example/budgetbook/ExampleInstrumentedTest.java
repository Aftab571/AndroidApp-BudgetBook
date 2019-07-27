package com.example.budgetbook;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.PinServImpl;
import com.example.budgetbook.service.PinService;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//        PinService srv = new PinServImpl();
//        FixedVO data =srv.getPin(appContext);
//        assertEquals(3333,data.getPin());

        //assertEquals("com.example.budgetbook", appContext.getPackageName());
    }
}
