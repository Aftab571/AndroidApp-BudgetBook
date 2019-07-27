package com.example.budgetbook.service;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class LimitServiceImplTest {
    public Context context= ApplicationProvider.getApplicationContext();
    LimitService serv = new LimitServiceImpl();
    @Test
    public void addLimitData() {
        boolean result=serv.addLimitData("Food",1500, context,null);
        assertEquals(true,result);
    }

}