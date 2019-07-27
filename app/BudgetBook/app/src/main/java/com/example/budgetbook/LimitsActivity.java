package com.example.budgetbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.budgetbook.service.LimitService;
import com.example.budgetbook.service.LimitServiceImpl;

import java.text.DecimalFormat;

public class LimitsActivity extends AppCompatActivity {

    Context mContext;
    private Spinner item;
    private EditText limitAmt;
    public TableLayout stk;
    private ImageButton mNext;
    private ImageButton mBack;
    private String frmDash;

    private static DecimalFormat df = new DecimalFormat("0.00");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limits);
        mContext = getApplicationContext();
        item = findViewById(R.id.unfxd_dropdown);
        limitAmt = findViewById(R.id.limit_amount);
        stk = findViewById(R.id.limit_table);
        mNext = findViewById(R.id.next2);
        mBack = findViewById(R.id.back_btn3);
        createTable(true);
        Intent i = getIntent();
        frmDash= i.getStringExtra("fromDash");
        if(frmDash!=null && frmDash.equals("Y")){
            mNext.setVisibility(View.INVISIBLE);
            mBack.setVisibility(View.VISIBLE);
        }
        else{
            mNext.setVisibility(View.VISIBLE);
            mBack.setVisibility(View.INVISIBLE);
        }

    }

    public void addExpLimits(View v) {
        float limit=0f;
        boolean result=false;
        LimitService srv = new LimitServiceImpl();
        String limit_val = limitAmt.getText().toString();
        if(limit_val!=null && !limit_val.isEmpty()) {
            limit = Math.round(Float.parseFloat(limit_val) * 100.0) / 100.0f;
        }
        String limit_comp = item.getSelectedItem().toString();
        if(limit>0 && limit_comp!=null && !limit_comp.isEmpty()){
            result =srv.addLimitData(limit_comp,limit,mContext,stk);
            if(result){
                limitAmt.getText().clear();
                stk.removeAllViews();
                createTable(true);
            }
            else {
                Toast toast = Toast.makeText(this,"Limit already added", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,-690);
                toast.show();
            }
        }
        else{
            if(limit==0){
                Toast toast = Toast.makeText(this, "Expenditure cannot be Zero", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }
            else {
                Toast toast = Toast.makeText(this, "Enter All mandatory fields", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }
        }


        Log.d("Add Fixed Exp","Result is : "+result);

    }

    public void createTable(boolean isAdd){

        LimitService srv = new LimitServiceImpl();
        srv.createTab(mContext,stk,isAdd);
    }

    public void clickNext(View v){

            Intent i = new Intent(LimitsActivity.this, DashboardActivity.class);
            startActivity(i);

    }

    public  void clickBack (View v){
        Intent i = new Intent(LimitsActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}
