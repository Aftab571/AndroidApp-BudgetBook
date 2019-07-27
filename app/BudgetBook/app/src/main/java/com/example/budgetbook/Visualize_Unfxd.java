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
import com.example.budgetbook.dao.UnFxdDao;
import com.example.budgetbook.dao.UnFxdDaoImpl;
import com.example.budgetbook.model.FilterVO;
import com.example.budgetbook.model.FixedVO;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Visualize_Unfxd extends AppCompatActivity implements Serializable {

    public static final String TAG="Visualize_Fixed";
    PieChart unfixedChart;
    ArrayList<FixedVO> unfixedList;
    List<FixedVO> incList;
    private Context mContext;
    private String to_date;
    private String from_date;
    ArrayList<FixedVO> sortedList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize__unfxd);
        unfixedChart = findViewById(R.id.unfixed_chart);
        //unfixedChart.setContentDescription("Fixed Expenditure");
        unfixedChart.setRotationEnabled(true);
        unfixedChart.setHoleRadius(25f);
        unfixedChart.setCenterText("Un-Fixed Expenditure");
        unfixedChart.setTransparentCircleAlpha(0);
        unfixedChart.setDrawEntryLabels(true);
        mContext = this.getApplicationContext();
        Intent i = this.getIntent();

        unfixedList= (ArrayList<FixedVO>)i.getSerializableExtra("unfxdList");
        to_date= i.getStringExtra("todate");
        from_date= i.getStringExtra("fromdate");
        addFixedSet(unfixedChart, mContext,unfixedList);

        unfixedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG,"e values is"+e.toString());
                Log.d(TAG,"h values is"+h.toString());
                String comp=null;
                Float amt= null;
                for(int i=0;i<sortedList.size();i++){
                    if(h.getX()==i){
                        comp=sortedList.get(i).getComponent();
                        amt= sortedList.get(i).getUnfxd_amt();
                        break;
                    }
                }

                Toast toast =  Toast.makeText(Visualize_Unfxd.this,"Component : "+ comp + "\n"+"Amount : "+ amt,Toast.LENGTH_LONG);
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
        UnFxdDao dao = new UnFxdDaoImpl(context);
        FixedCompDaoHandler fxdao = new DataBaseHandlerImpl(context);
        if(dataList!=null && !dataList.isEmpty() && dataList.size()>0){
            unfixedList = dataList;
            incList= ((DataBaseHandlerImpl) fxdao).getIncomeFilter(context,from_date,to_date);
        }
        else{

            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar cal = Calendar.getInstance();
            int month =cal.get(Calendar.MONTH);
            int year =cal.get(Calendar.YEAR);
            String start_date = "01-"+(month<10?"0"+(month+1):(month+1))+"-"+year;
            Date today = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            Date lastDayOfMonth = calendar.getTime();

            String end_date = sdf.format(lastDayOfMonth);
            incList= ((DataBaseHandlerImpl) fxdao).getIncomeFilter(context,start_date,end_date);
            FilterVO vo = new FilterVO();
            vo.setFrom(start_date);
            vo.setTo(end_date);
            unfixedList = ((UnFxdDaoImpl) dao).getUnFxdTableFilterData(vo);

        }

       // incList= ((DataBaseHandlerImpl) fxdao).getIncomeFilter(context,from_date,to_date);

        if(unfixedList!=null && unfixedList.size()>0) {
            float inc = 0f;
            for (FixedVO data : incList) {
                inc = inc + data.getIncome();
            }

            float food_amt = 0f;
            float Super_amt = 0f;
            float Medical_amt = 0f;
            float Travel_amt = 0f;
            float Entertainment_amt = 0f;
            float Education_amt = 0f;
            float Shopping_amt = 0f;
            float Sports_amt = 0f;
            float Others_amt = 0f;


            for (FixedVO data : unfixedList) {
                if (data.getComponent().equals("Food")) {
                    food_amt = food_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Super market")) {
                    Super_amt = Super_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Medical")) {
                    Medical_amt = Medical_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Travel")) {
                    Travel_amt = Travel_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Entertainment")) {
                    Entertainment_amt = Entertainment_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Education")) {
                    Education_amt = Education_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Shopping")) {
                    Shopping_amt = Shopping_amt + data.getUnfxd_amt();
                } else if (data.getComponent().equals("Sports & Fitness")) {
                    Sports_amt = Sports_amt + data.getUnfxd_amt();
                } else {
                    Others_amt = Others_amt + data.getUnfxd_amt();
                }
            }
            float[] amts = {food_amt, Super_amt, Medical_amt, Travel_amt, Entertainment_amt, Education_amt, Shopping_amt, Sports_amt, Others_amt};
            String[] comp = {"Food", "Super market", "Medical", "Travel", "Entertainment", "Education", "Shopping", "Sports &amp; Fitness", "Others"};
            for (int i = 0; i < comp.length; i++) {
                FixedVO fvo = new FixedVO();
                if (amts[i] > 0) {
                    fvo.setComponent(comp[i]);
                    fvo.setUnfxd_amt(amts[i]);
                    sortedList.add(fvo);
                }
            }
            if (inc > 0) {
                for (FixedVO data : sortedList) {
                    float percent = Math.round((data.getUnfxd_amt() / inc) * 100f);
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
               // pieDataSet.setSliceSpace(2);
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
                colors.add(Color.WHITE);
                colors.add(Color.parseColor("#750b88"));
                colors.add(Color.CYAN);

                pieDataSet.setColors(colors);


                // Legend legend = unfixedChart.getLegend();
                //legend.setForm(Legend.LegendForm.CIRCLE);
                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextColor(Color.BLACK);
                pieData.setValueTextSize(13f);
                unfixedChart.getLegend().setTextColor(Color.BLACK);
                unfixedChart.getLegend().setWordWrapEnabled(true);

                unfixedChart.setData(pieData);
                unfixedChart.invalidate();
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
        Intent i = new Intent(Visualize_Unfxd.this, FilterActivity.class);
        i.putExtra("src","UNFXD");
        startActivity(i);
    }

    public void backNav(View v){
        Intent i = new Intent(Visualize_Unfxd.this, DashboardActivity.class);
        startActivity(i);
    }
}
