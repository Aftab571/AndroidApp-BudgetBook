package com.example.budgetbook;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.SoundPool;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.FixedCompService;
import com.example.budgetbook.service.FixedCompServiceImpl;
import com.example.budgetbook.utility.CommonUtility;

import java.text.DecimalFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FixedActivity extends AppCompatActivity {
    private static final String TAG = "FixedActivity";
    private Context mContext;
    private Activity mActivity;
    private FixedCompDaoHandler mDataBaseHandlerImpl;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private ImageButton mDatePicker;
    private Spinner mPayFreq;
    private Spinner mPayMode;
    private EditText mfixedAmt, mIncome;
    private Spinner mFxdDrpDown;
    public TableLayout stk;
    private ImageButton mNext;
    private ImageButton mBack;
    private String frmDash;
    public Constants constants = new Constants();
    public CommonUtility utility = new CommonUtility();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixed);
        mNext = findViewById(R.id.next);
        mBack = findViewById(R.id.back_btn2);
        mContext = getApplicationContext();
        mActivity = FixedActivity.this;

        mPayFreq = findViewById(R.id.pay_frequency);
        mPayMode = findViewById(R.id.paymentMode);

        mfixedAmt = findViewById(R.id.fixed_amount);

        mFxdDrpDown = findViewById(R.id.fixed_dropdown);

        mDisplayDate = findViewById(R.id.Fxd_date);
        mDatePicker = findViewById(R.id.fxd_date);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        FixedActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "date is : " + dayOfMonth + "/" + (month + 1) + "/" + year);
                mDisplayDate.setText(dayOfMonth + "-" + "0" + (month + 1) + "-" + year);
            }
        };


        stk = findViewById(R.id.fxd_table);

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

    public void addFixedExp(View v) {
        try {
            boolean result = false;
            float fix_amt=0f;
            FixedCompService srv = new FixedCompServiceImpl();
            String pay_freq = mPayFreq.getSelectedItem().toString();
            String pay_mode = mPayMode.getSelectedItem().toString();
            String fdate = mDisplayDate.getText().toString();
            String fix_amt_val = mfixedAmt.getText().toString();
            if(fix_amt_val!=null && !fix_amt_val.isEmpty()) {
                 fix_amt = Math.round(Float.parseFloat(fix_amt_val) * 100.0) / 100.0f;
            }
            String fxd_comp_val = mFxdDrpDown.getSelectedItem().toString();
            FixedVO vo = new FixedVO();
            vo.setPayMode(pay_mode);
            vo.setFxd_amt(fix_amt);
            vo.setComponent(fxd_comp_val);
            vo.setPayDate(fdate);
            vo.setFreq(pay_freq);
            if (pay_mode != null && !pay_mode.isEmpty() && fix_amt > 0 && fxd_comp_val != null && !fxd_comp_val.isEmpty() && fdate != null && !fdate.isEmpty() && pay_freq != null && !pay_freq.isEmpty()) {
                String regex = constants.date_reg;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(fdate);
                if (matcher.matches()) {
                    if(utility.isDateValid(fdate)) {
                        result = srv.addFixedComponents(vo, mContext, stk);
                        if (result) {
                            mfixedAmt.getText().clear();
                            mDisplayDate.setText("");
                            stk.removeAllViews();
                            createTable(true);
                        } else {
                            Toast toast = Toast.makeText(this, "Expenditure already added", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();
                        }
                    }
                    else{
                        Toast toast = Toast.makeText(this, "Date Does not exist in the Calendar", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, -690);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -690);
                    toast.show();
                }
            } else {
                if(fix_amt==0){
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



            Log.d("Add Fixed Exp", "Result is : " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createTable(boolean isAdd) {

        FixedCompService srv = new FixedCompServiceImpl();
        srv.createTab(mContext, stk, isAdd);
    }

    public void nextClick(View v) {

            Intent i = new Intent(FixedActivity.this, LimitsActivity.class);
            startActivity(i);
    }

    public void clickBack(View v) {
        Intent i = new Intent(FixedActivity.this, DashboardActivity.class);
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
