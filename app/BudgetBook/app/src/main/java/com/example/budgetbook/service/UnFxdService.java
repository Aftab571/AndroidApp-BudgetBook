package com.example.budgetbook.service;

import android.content.Context;
import android.widget.TableLayout;

import com.example.budgetbook.model.FixedVO;

public interface UnFxdService {

    boolean addUnfxdData(FixedVO vo, Context mContext, TableLayout stk);

    void createTab(Context mContext, TableLayout stk, boolean isAdd);
}
