package com.example.budgetbook.service;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class UnFxdServiceImplTest {
    public Context mockContext = ApplicationProvider.getApplicationContext();
    UnFxdService serv = new UnFxdServiceImpl();
    @Test
    public void addUnfxdData() {
        FixedVO vo = new FixedVO();
        vo.setComponent("Food");
        vo.setPayMode("Cash");
        vo.setUnfxd_amt(1000);
        vo.setPayDate("01-07-2019");
        vo.setComment("from Test");
        boolean result =serv.addUnfxdData(vo,mockContext,null);
        assertEquals(true,result);
    }


}