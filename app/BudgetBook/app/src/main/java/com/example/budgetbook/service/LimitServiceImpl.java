package com.example.budgetbook.service;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.budgetbook.R;
import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.LimitDao;
import com.example.budgetbook.dao.LimitDaoImpl;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.utility.CommonUtility;

import java.util.ArrayList;

public class LimitServiceImpl extends CommonUtility implements LimitService {

    Constants constants = new Constants();
    ArrayList<FixedVO> fxd_list = new ArrayList<>();
    @Override
    public boolean addLimitData(String comp, float fxdAmt, Context context, TableLayout tb) {

        LimitDao db = new LimitDaoImpl(context);
        long isInserted = db.insertLimCompinDB(comp,fxdAmt);
        return isInserted != -1;
    }

    @Override
    public void createTab(Context context, TableLayout tab , boolean isAdd) {
        LimitDao dao = new LimitDaoImpl(context);
        final TableLayout tab_lay = tab;
        final Context contex = context;
        fxd_list = dao.getLimTableData();
//        if(fxd_list.size()>0){
//            next.setText("CANCEL");
//        }
        createTabComm(context,tab,isAdd,fxd_list,"LIMIT",false);
    }

    public boolean deleteLim(String name, Context context){

        boolean result = false;
        LimitDao db = new LimitDaoImpl(context);
        long val = db.deleteLim(name);
        result = val > 0;
        return result;
    }
}
