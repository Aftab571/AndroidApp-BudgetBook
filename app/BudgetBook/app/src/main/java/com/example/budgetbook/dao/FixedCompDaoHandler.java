package com.example.budgetbook.dao;

import android.content.Context;

import com.example.budgetbook.model.FixedVO;

import java.util.ArrayList;

public interface FixedCompDaoHandler {

    long insertFxdCompinDB(FixedVO data);

    ArrayList<FixedVO> getFxdTableData();

    long deleteFxd(int name);

    long deleteInc(String date);

    long addIncome(Context mContext, float income,String idate);

    ArrayList<FixedVO> getIncome(Context mContext);
}
