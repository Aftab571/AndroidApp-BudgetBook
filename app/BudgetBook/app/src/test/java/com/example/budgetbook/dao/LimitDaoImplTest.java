package com.example.budgetbook.dao;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class LimitDaoImplTest {
    public Context context= ApplicationProvider.getApplicationContext();
    LimitDao dao = new LimitDaoImpl(context);
    @Test
    public void getLimTableData() {
        ArrayList<FixedVO> result =dao.getLimTableData();
        assertNotNull(result);
    }

    @Test
    public void deleteLim() {
        long result =dao.deleteLim("Food");
        assertNotNull(result);
    }
}