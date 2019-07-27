package com.example.budgetbook.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.budgetbook.model.FixedVO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.io.IOException;
import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class PinServImplTest {
    public Context mockContext = ApplicationProvider.getApplicationContext();

    @Test
    public void getPin() {

        PinService srv = new PinServImpl();
        FixedVO data = srv.getPin(mockContext);
        assertNotNull(data.getPin());
    }

}