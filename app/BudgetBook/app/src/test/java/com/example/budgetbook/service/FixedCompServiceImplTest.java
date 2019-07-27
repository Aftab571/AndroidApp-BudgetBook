package com.example.budgetbook.service;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class FixedCompServiceImplTest {
    public Context context= ApplicationProvider.getApplicationContext();
    FixedCompService serv = new FixedCompServiceImpl();
    @Test
    public void addFixedComponents() {
        FixedVO data = new FixedVO();
        data.setFxd_amt(1000);
        data.setComponent("Loan");
        data.setPayDate("01-08-2019");
        data.setFreq("Monthly");
        data.setPayMode("Cash");
        boolean result = serv.addFixedComponents(data,context,null);
        assertEquals(true,result);
    }

    @Test
    public void addIncome() {
        boolean result =serv.addIncome(context,100000,"01-09-2019");
        assertEquals(true,result);
    }

    @Test
    public void getIncome() {
       ArrayList<FixedVO> list = serv.getIncome(context);
       assertNotNull(list);

    }

}