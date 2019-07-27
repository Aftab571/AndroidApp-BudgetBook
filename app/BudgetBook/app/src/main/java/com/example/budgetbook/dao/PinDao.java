package com.example.budgetbook.dao;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

public interface PinDao {
    FixedVO getPin(Context context);

    long insertPin(Context mcontext, int entered_pin);

    long deletePin(Context mcontext, int pin);
}
