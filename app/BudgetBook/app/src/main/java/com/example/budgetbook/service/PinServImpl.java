package com.example.budgetbook.service;

import android.content.Context;

import com.example.budgetbook.dao.PinDao;
import com.example.budgetbook.dao.PinDaoImpl;
import com.example.budgetbook.model.FixedVO;

public class PinServImpl implements PinService{

    @Override
    public FixedVO getPin(Context context) {
        PinDao pdao= new PinDaoImpl(context);
        FixedVO pin_data=pdao.getPin(context);
        return pin_data;
    }

    @Override
    public boolean insertPin(Context mcontext, int entered_pin) {
        PinDao pdao= new PinDaoImpl(mcontext);
        long result=pdao.insertPin(mcontext,entered_pin);
        return result != -1;

    }
}
