package com.example.budgetbook.service;

import android.content.Context;
import android.widget.TableLayout;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.dao.LimitDao;
import com.example.budgetbook.dao.LimitDaoImpl;
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.utility.CommonUtility;

import java.util.ArrayList;

public class UnFxdServiceImpl extends CommonUtility implements UnFxdService{

    Constants constants = new Constants();
    ArrayList<FixedVO> fxd_list = new ArrayList<>();
    @Override
    public boolean addUnfxdData(FixedVO data, Context context, TableLayout tb) {

        UnFxdDao db = new UnFxdDaoImpl(context);
        long isInserted = db.insertUnfxdCompinDB(data);
        return isInserted != -1;
    }

    @Override
    public void createTab(Context context, TableLayout tab , boolean isAdd) {
        UnFxdDao db = new UnFxdDaoImpl(context);
        final TableLayout tab_lay = tab;
        final Context contex = context;
        fxd_list = db.getUnfxdTableData();
        createTabComm(context,tab,isAdd,fxd_list,"UNFXD",false);
    }

    public boolean deleteUnfxd(int name, Context context){

        boolean result = false;
        UnFxdDao db = new UnFxdDaoImpl(context);
        long val = db.deleteUnfxd(name);
        result = val > 0;
        return result;
    }

}
