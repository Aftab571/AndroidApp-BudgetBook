package com.example.budgetbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Visualize_Fixed extends AppCompatActivity implements Serializable {
        public static final String TAG="Visualize_Fixed";
        PieChart fixedChart;
        ArrayList<FixedVO> fixedList;
        List<FixedVO> incList;
        private Context mContext;
        private String to_date;
        private String from_date;
        ArrayList<FixedVO> sortedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize__fixed);
        fixedChart = findViewById(R.id.fixed_chart);
        //fixedChart.setContentDescription("Fixed Expenditure");
        fixedChart.setRotationEnabled(true);
        fixedChart.setHoleRadius(25f);
        fixedChart.setCenterText("Fixed Expenditure");
        fixedChart.setTransparentCircleAlpha(0);
        fixedChart.setDrawEntryLabels(true);
        mContext = this.getApplicationContext();
        Intent i = this.getIntent();

        fixedList= (ArrayList<FixedVO>)i.getSerializableExtra("fxdList");
        to_date= i.getStringExtra("todate");
        from_date= i.getStringExtra("fromdate");
        addFixedSet(fixedChart, mContext,fixedList);

        fixedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG,"e values is"+e.toString());
                Log.d(TAG,"h values is"+h.toString());
                String comp=null;
                Float amt= null;
                for(int i=0;i<sortedList.size();i++){
                    if(h.getX()==i){
                        comp=sortedList.get(i).getComponent();
                        amt= sortedList.get(i).getFxd_amt();
                        break;
                    }
                }

                Toast toast =  Toast.makeText(Visualize_Fixed.this,"Component : "+ comp + "\n"+"Amount : "+ amt,Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,-690);
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    public void addFixedSet(PieChart chart, Context context,ArrayList<FixedVO> dataList) {
        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();
        FixedCompDaoHandler dao = new DataBaseHandlerImpl(context);
        if (dataList != null && !dataList.isEmpty() && dataList.size() > 0) {
            fixedList = dataList;
            incList = ((DataBaseHandlerImpl) dao).getIncomeFilter(context, from_date, to_date);
        } else {

            Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            String start_date = "01-" + (month < 10 ? "0" + (month + 1) : (month + 1)) + "-" + year;
            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            Date lastDayOfMonth = calendar.getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String end_date = sdf.format(lastDayOfMonth);
            incList = ((DataBaseHandlerImpl) dao).getIncomeFilter(context, start_date, end_date);
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            fixedList = ((DataBaseHandlerImpl) dao).getFxdTableFilterData(vo);
        }
        if(fixedList!=null && fixedList.size()>0) {

            float inc = 0f;
            for (FixedVO data : incList) {
                inc = inc + data.getIncome();
            }

            float rent_amt = 0f;
            float Electricity_amt = 0f;
            float Water_amt = 0f;
            float Loan_amt = 0f;
            float Internet_amt = 0f;
            float Others_amt = 0f;

            for (FixedVO data : fixedList) {
                if (data.getComponent().equals("Rent")) {
                    rent_amt = rent_amt + data.getFxd_amt();
                } else if (data.getComponent().equals("Electricity Bill")) {
                    Electricity_amt = Electricity_amt + data.getFxd_amt();
                } else if (data.getComponent().equals("Water Bill")) {
                    Water_amt = Water_amt + data.getFxd_amt();
                } else if (data.getComponent().equals("Loan")) {
                    Loan_amt = Loan_amt + data.getFxd_amt();
                } else if (data.getComponent().equals("Internet Bill")) {
                    Internet_amt = Internet_amt + data.getFxd_amt();
                } else {
                    Others_amt = Others_amt + data.getFxd_amt();
                }
            }
            float[] amts = {rent_amt, Electricity_amt, Water_amt, Loan_amt, Internet_amt, Others_amt};
            String[] comp = {"Rent", "Electricity Bill", "Water Bill", "Loan", "Internet Bill", "Others"};
            for (int i = 0; i < comp.length; i++) {
                FixedVO fvo = new FixedVO();
                if (amts[i] > 0) {
                    fvo.setComponent(comp[i]);
                    fvo.setFxd_amt(amts[i]);
                    sortedList.add(fvo);
                }
            }
            if (inc > 0) {
                for (FixedVO data : sortedList) {
                    float percent = Math.round((data.getFxd_amt() / inc) * 100f);
                    String disPerct = "% \n" + data.getComponent();
                    //String disPerct = "%";
                    chart.setEntryLabelColor(Color.BLACK);
                    chart.setEntryLabelTextSize(13f);
                    chart.setCenterTextSize(15f);
                    yEntrys.add(new PieEntry(percent, disPerct));
                }
                for (FixedVO data : sortedList) {
                    xEntrys.add(data.getComponent());
                }

                PieDataSet pieDataSet = new PieDataSet(yEntrys, "of Total Income");
                pieDataSet.setSliceSpace(2);
                pieDataSet.setValueTextSize(14);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueLineColor(Color.BLACK);
                ArrayList<Integer> colors = new ArrayList<>();
                colors.add(Color.BLUE);
                colors.add(Color.RED);
                colors.add(Color.GRAY);
                colors.add(Color.GREEN);
                colors.add(Color.YELLOW);
                colors.add(Color.MAGENTA);

                pieDataSet.setColors(colors);


                // Legend legend = fixedChart.getLegend();
                //legend.setForm(Legend.LegendForm.CIRCLE);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextColor(Color.BLACK);
                pieData.setValueTextSize(13f);
                fixedChart.getLegend().setTextColor(Color.BLACK);
                fixedChart.getLegend().setWordWrapEnabled(true);
                fixedChart.setData(pieData);
                fixedChart.invalidate();
            } else {
                Toast toast = Toast.makeText(this, "Income not available for the current Month/ selected period", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }
        }
        else {
            Toast toast = Toast.makeText(this, "No Un-Fixed Expenditure available", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, -690);
            toast.show();
        }


    }

    public void clickFilter(View v){
        Intent i = new Intent(Visualize_Fixed.this, FilterActivity.class);
        i.putExtra("src","FIXED");
        startActivity(i);
    }

    public void backNav(View v){
        Intent i = new Intent(Visualize_Fixed.this, DashboardActivity.class);
        startActivity(i);
    }
}
