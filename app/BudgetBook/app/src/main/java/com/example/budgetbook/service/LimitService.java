package com.example.budgetbook.service;

import android.content.Context;
import android.widget.Button;
import android.widget.TableLayout;

public interface LimitService {
    boolean addLimitData(String limit_comp, float limit_val, Context mContext, TableLayout stk);

    void createTab(Context mContext, TableLayout stk, boolean isAdd);
}
