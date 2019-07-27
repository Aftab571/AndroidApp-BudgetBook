package com.example.budgetbook.service;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;

import com.example.budgetbook.model.FixedVO;

import java.util.ArrayList;

public interface FixedCompService {

    boolean addFixedComponents(FixedVO data, Context context, TableLayout tb);

    void createTab(Context context, TableLayout stk, boolean isAdd);

    void createTabInc(Context context, TableLayout stk, boolean isAdd);

    boolean deleteFxd(int name, Context context);

    boolean addIncome(Context mContext, float income,String idate);

    ArrayList<FixedVO> getIncome(Context mContext);

}
