package com.example.budgetbook.utility;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.budgetbook.R;
import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.LimitDao;
import com.example.budgetbook.dao.LimitDaoImpl;
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.FixedCompService;
import com.example.budgetbook.service.FixedCompServiceImpl;
import com.example.budgetbook.service.LimitService;
import com.example.budgetbook.service.LimitServiceImpl;
import com.example.budgetbook.service.UnFxdService;
import com.example.budgetbook.service.UnFxdServiceImpl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



public class CommonUtility {
    final static String DATE_FORMAT = "dd-MM-yyyy";

    public void createTabComm(Context context, TableLayout tab, boolean isAdd, ArrayList<FixedVO> fxdList, String module,boolean isFil) {

        FixedCompDaoHandler dao = new DataBaseHandlerImpl(context);
        final TableLayout tab_lay = tab;
        final Context contex = context;
        final String mod = module;
        final ArrayList<FixedVO> fxd_list = fxdList;

        //fxd_list = dao.getFxdTableData();
        if (fxd_list.size() > 0) {
            TableLayout stk = tab;
            if (isAdd) {
                int size = 16;
                String HeadColor = "#FF00574B";
                TableRow tbrow0 = new TableRow(context);
                TextView tv0 = new TextView(context);

                tv0.setText(" S.NO ");
                tv0.setTextColor(Color.parseColor(HeadColor));
                tv0.setTextSize(size);
                tv0.setTypeface(Typeface.DEFAULT_BOLD);
                tbrow0.addView(tv0);
                if(!mod.equals("INC")) {
                    TextView tv1 = new TextView(context);
                    tv1.setText("   COMPONENT ");
                    tv1.setTextColor(Color.parseColor(HeadColor));
                    tv1.setTextSize(size);
                    tv1.setTypeface(Typeface.DEFAULT_BOLD);
                    tbrow0.addView(tv1);
                }

                TextView tv2 = new TextView(context);
                if(mod.equals("UNFXD") || mod.equals("FIXED")){
                    tv2.setText(" EXPENDITURE ");
                }
                else if(mod.equals("INC")){
                    tv2.setText(" INCOME ");
                }
                else{
                    tv2.setText(" LIMITS ");
                }

                tv2.setTextColor(Color.parseColor(HeadColor));
                tv2.setTextSize(size);
                tv2.setTypeface(Typeface.DEFAULT_BOLD);
                tbrow0.addView(tv2);

                if (mod.equals("UNFXD") || mod.equals("FIXED")) {
                    TextView tv5 = new TextView(context);
                    tv5.setText(" PAY MODE ");
                    tv5.setTextColor(Color.parseColor(HeadColor));
                    tv5.setTextSize(size);
                    tv5.setGravity(1);
                    tv5.setTypeface(Typeface.DEFAULT_BOLD);
                    tbrow0.addView(tv5);


                }
                if (mod.equals("UNFXD") || mod.equals("FIXED") || mod.equals("INC")) {
                    TextView tv7 = new TextView(context);
                    tv7.setText(" DATE ");
                    tv7.setTextColor(Color.parseColor(HeadColor));
                    tv7.setTextSize(size);
                    tv7.setGravity(1);
                    tv7.setTypeface(Typeface.DEFAULT_BOLD);
                    tbrow0.addView(tv7);
                }
                if (mod.equals("FIXED")) {
                    TextView tv6 = new TextView(context);
                    tv6.setText(" FREQUENCY ");
                    tv6.setTextColor(Color.parseColor(HeadColor));
                    tv6.setTextSize(size);
                    tv6.setTypeface(Typeface.DEFAULT_BOLD);
                    tbrow0.addView(tv6);


                }
                if (mod.equals("UNFXD")) {
                    TextView tv4 = new TextView(context);
                    tv4.setText(" COMMENT ");
                    tv4.setGravity(1);
                    tv4.setTextColor(Color.parseColor(HeadColor));
                    tv4.setTextSize(size);
                    tv4.setTypeface(Typeface.DEFAULT_BOLD);
                    tv4.setWidth(300);
                    tbrow0.addView(tv4);
                }

                if(!isFil) {
                    TextView tv3 = new TextView(context);
                    tv3.setText(" ACTION ");
                    tv3.setTextColor(Color.parseColor(HeadColor));
                    tv3.setTextSize(size);
                    tv3.setTypeface(Typeface.DEFAULT_BOLD);
                    tbrow0.addView(tv3);
                }
                    stk.addView(tbrow0);

            }
            int counter = 1;
            for (int i = 0; i < fxd_list.size(); i++) {
                TableRow tbrow = new TableRow(context);
                TextView t1v = new TextView(context);
                t1v.setText(""+counter++);
                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);
                if(!mod.equals("INC")) {
                    TextView t2v = new TextView(context);
                    t2v.setText(fxd_list.get(i).getComponent());
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);
                }

                TextView t3v = new TextView(context);
                if (mod.equals("UNFXD")) {
                    t3v.setText(String.valueOf(fxd_list.get(i).getUnfxd_amt()));
                } else if (mod.equals("FIXED")) {
                    t3v.setText(String.valueOf(fxd_list.get(i).getFxd_amt()));
                } else if (mod.equals("INC")) {
                    t3v.setText(String.valueOf(fxd_list.get(i).getIncome()));
                } else {
                    t3v.setText(String.valueOf(fxd_list.get(i).getLim_amt()));
                }
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);

                if (mod.equals("UNFXD") || mod.equals("FIXED")) {
                    TextView t5v = new TextView(context);
                    t5v.setText(String.valueOf(fxd_list.get(i).getPayMode()));
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.CENTER);
                    tbrow.addView(t5v);
                }





                if (mod.equals("UNFXD") || mod.equals("FIXED") || mod.equals("INC")){
                    TextView t7v = new TextView(context);
                    if(mod.equals("INC")){
                        t7v.setText(String.valueOf(fxd_list.get(i).getiDate()));
                    }
                    else{
                        t7v.setText(String.valueOf(fxd_list.get(i).getPayDate()));
                    }

                    t7v.setTextColor(Color.BLACK);
                    t7v.setGravity(Gravity.CENTER);
                    tbrow.addView(t7v);
                }

                if(mod.equals("FIXED")){
                    TextView t6v = new TextView(context);
                    t6v.setText(String.valueOf(fxd_list.get(i).getFreq()));
                    t6v.setTextColor(Color.BLACK);
                    t6v.setGravity(Gravity.CENTER);
                    tbrow.addView(t6v);
                }

                if (mod.equals("UNFXD")) {
                    TextView t4v = new TextView(context);
                    if (fxd_list.get(i).getComment() != null && !fxd_list.get(i).getComment().isEmpty()) {
                        t4v.setText(String.valueOf(fxd_list.get(i).getComment()));
                        t4v.setWidth(300);
                    } else {
                        t4v.setText("--");
                    }

                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    tbrow.addView(t4v);
                }
                if(!isFil) {
                    Button del_btn = new Button(context);
                    del_btn.setId(i);
                    del_btn.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.drawable.delete), null, null, null);
                    del_btn.setHeight(10);
                    del_btn.setWidth(20);
                    del_btn.setBackgroundColor(0);

                    del_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int id = v.getId();
                            ArrayList<FixedVO> myList = new ArrayList<>();
                            Log.d("id", "is" + id);
                            if (mod.equals("FIXED")) {
                                FixedCompService fx = new FixedCompServiceImpl();
                                FixedCompDaoHandler dao = new DataBaseHandlerImpl(contex);
                                fx.deleteFxd(fxd_list.get(id).getID(), contex);
                                myList = dao.getFxdTableData();

                            } else if (mod.equals("LIMIT")) {
                                LimitService ls = new LimitServiceImpl();
                                LimitDao dao = new LimitDaoImpl(contex);
                                ((LimitServiceImpl) ls).deleteLim(fxd_list.get(id).getComponent(), contex);
                                myList = dao.getLimTableData();
                            } else if (mod.equals("INC")) {
                                FixedCompDaoHandler dao = new DataBaseHandlerImpl(contex);
                                dao.deleteInc(fxd_list.get(id).getiDate());
                                myList = dao.getIncome(contex);
                            } else {
                                UnFxdService ufx = new UnFxdServiceImpl();
                                UnFxdDao dao = new UnFxdDaoImpl(contex);
                                ((UnFxdServiceImpl) ufx).deleteUnfxd(fxd_list.get(id).getID(), contex);
                                myList = dao.getUnfxdTableData();
                            }

                            tab_lay.removeAllViews();
                            createTabComm(contex, tab_lay, true, myList, mod,false);
                        }
                    });
                    tbrow.addView(del_btn);
                }
                stk.addView(tbrow);
            }

        } else {
            Log.d("Create table ", "No data found in fixed table");
        }
    }

    public boolean isDateValid(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public float getTotal(ArrayList<FixedVO> data,String src){
        float total=0f;
        if(src.equals("INC")){
            for(FixedVO x : data){
                total = total+x.getIncome();
            }
        }
        else if(src.equals("FXD")){
            for(FixedVO x : data){
                total = total+x.getFxd_amt();
            }
        }
        else{
            for(FixedVO x : data){
                total = total+x.getUnfxd_amt();
            }
        }

        return total;
    }



}
