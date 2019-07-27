package com.example.budgetbook.dao;

import com.example.budgetbook.model.FixedVO;

import java.util.ArrayList;

public interface LimitDao {
    long deleteLim(String name);

    ArrayList<FixedVO> getLimTableData();

    long insertLimCompinDB(String comp, float fxdAmt);
}
