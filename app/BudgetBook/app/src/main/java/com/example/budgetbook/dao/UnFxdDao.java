package com.example.budgetbook.dao;

import com.example.budgetbook.model.FixedVO;

import java.util.ArrayList;

public interface UnFxdDao {

    long deleteUnfxd(int name);

    ArrayList<FixedVO> getUnfxdTableData();

    long insertUnfxdCompinDB(FixedVO data);

}
