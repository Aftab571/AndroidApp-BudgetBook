package com.example.budgetbook.service;

import android.content.Context;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.utility.CommonUtility;

import java.util.ArrayList;
import java.util.List;

public class FilterServiceImpl extends CommonUtility {

    public void createTable(Context context, TableLayout tab, boolean isAdd, Button next,ArrayList<FixedVO> dataList,String src){
        createTabComm(context,tab,isAdd,dataList,src,true);

    }

    public ArrayList<FixedVO> searchData(FilterVO data, String src, Context context){

        ArrayList<FixedVO> dataList = new ArrayList<>();
        if(src.equals("FIXED")){
            FixedCompDaoHandler dao = new DataBaseHandlerImpl(context);
            dataList=((DataBaseHandlerImpl) dao).getFxdTableFilterData(data);
        }
        else{
            UnFxdDao dao = new UnFxdDaoImpl(context);
            dataList=((UnFxdDaoImpl) dao).getUnFxdTableFilterData(data);
        }

        return dataList;
    }
}
