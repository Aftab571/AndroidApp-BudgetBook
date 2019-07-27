package com.example.budgetbook.service;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.budgetbook.R;
import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.utility.CommonUtility;

import java.util.ArrayList;

public class FixedCompServiceImpl extends CommonUtility implements FixedCompService {

    Constants constants = new Constants();
    ArrayList<FixedVO> fxd_list = new ArrayList<>();
    @Override
    public boolean addFixedComponents(FixedVO data, Context context,TableLayout tb) {

        FixedCompDaoHandler db = new DataBaseHandlerImpl(context);
        long isInserted = db.insertFxdCompinDB(data);
        return isInserted != -1;
    }

    @Override
    public void createTab(Context context, TableLayout tab , boolean isAdd) {

        FixedCompDaoHandler dao = new DataBaseHandlerImpl(context);
        final TableLayout tab_lay = tab;
         final Context contex = context;
        fxd_list = dao.getFxdTableData();
//        if(fxd_list.size()>0){
//            next.setText("CANCEL");
//        }
        createTabComm(context,tab,isAdd,fxd_list,"FIXED",false);

    }

    @Override
    public void createTabInc(Context context, TableLayout tab , boolean isAdd) {

        FixedCompDaoHandler dao = new DataBaseHandlerImpl(context);
        final TableLayout tab_lay = tab;
        final Context contex = context;
        fxd_list = dao.getIncome(contex);
        createTabComm(context,tab,isAdd,fxd_list,"INC",false);

    }

    public boolean deleteFxd(int name, Context context){

        boolean result = false;
        FixedCompDaoHandler db = new DataBaseHandlerImpl(context);
        long val = db.deleteFxd(name);
        result = val > 0;
        return result;
    }

    @Override
    public boolean addIncome(Context mContext, float income,String idate) {
        boolean result;
        FixedCompDaoHandler db = new DataBaseHandlerImpl(mContext);
        long val=db.addIncome(mContext,income,idate);
        result = val > 0;
        return result;
    }

    @Override
    public ArrayList<FixedVO> getIncome(Context mContext) {
        FixedCompDaoHandler db = new DataBaseHandlerImpl(mContext);
        ArrayList<FixedVO> fx = new ArrayList<>();
        fx=db.getIncome(mContext);

        return fx;
    }


}


