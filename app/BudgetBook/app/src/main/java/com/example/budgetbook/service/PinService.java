package com.example.budgetbook.service;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

public interface PinService {

    FixedVO getPin(Context context);

    boolean insertPin(Context mcontext, int entered_pin);
}
