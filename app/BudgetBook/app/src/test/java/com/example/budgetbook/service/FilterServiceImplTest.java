package com.example.budgetbook.service;

import android.content.Context;

import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;
@RunWith(RobolectricTestRunner.class)
public class FilterServiceImplTest {
    public Context context= ApplicationProvider.getApplicationContext();
    FilterServiceImpl serv = new FilterServiceImpl();

    @Test
    public void createTable() {
    }

    @Test
    public void searchData() {
        FilterVO vo = new FilterVO();
        vo.setTo("31-07-2019");
        vo.setFrom("01-07-2019");
        ArrayList<FixedVO> list =serv.searchData(vo,"FIXED",context);
        assertNotNull(list);
    }

    @Test
    public void unFxdsearchData() {
        FilterVO vo = new FilterVO();
        vo.setTo("31-07-2019");
        vo.setFrom("01-07-2019");
        ArrayList<FixedVO> list =serv.searchData(vo,"UNFXD",context);
        assertNotNull(list);
    }
}