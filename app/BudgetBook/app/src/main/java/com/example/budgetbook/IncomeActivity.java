package com.example.budgetbook;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.service.FixedCompService;
import com.example.budgetbook.service.FixedCompServiceImpl;
import com.example.budgetbook.utility.CommonUtility;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncomeActivity extends AppCompatActivity {

    private static final String TAG = "IncomeActivity";
    private Button incAdd;
    public TableLayout stk;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private ImageButton mDatePicker;
    private EditText mIncome;
    private Context mContext;
    private String frmDash;
    public Constants constants = new Constants();
    private ImageButton mback;
    private ImageButton mnext;
    public CommonUtility utility = new CommonUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        mIncome = findViewById(R.id.incomeAmt);
        mnext = findViewById(R.id.next_btn);
        mback = findViewById(R.id.back_btn);
        mContext = this.getApplicationContext();
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
                        IncomeActivity.this,
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
        Intent i = getIntent();
        frmDash = i.getStringExtra("fromDash");
        if (frmDash != null && frmDash.equals("Y")) {
            mback.setVisibility(View.VISIBLE);
            mnext.setVisibility(View.INVISIBLE);
        } else {
            mback.setVisibility(View.INVISIBLE);
            mnext.setVisibility(View.VISIBLE);
        }
        getIncome();
    }

    public void addIncome(View v) {
        FixedCompService srv = new FixedCompServiceImpl();
        String income_val = mIncome.getText().toString();
        String iDate = mDisplayDate.getText().toString();
        if (income_val != null && !income_val.isEmpty() && iDate != null && !iDate.isEmpty()) {
            float income = Math.round(Float.parseFloat(income_val) * 100.0) / 100.0f;
            if(income>0) {
                String regex = constants.date_reg;
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(iDate);
                if (matcher.matches()) {
                    if (utility.isDateValid(iDate)) {
                        boolean result = srv.addIncome(mContext, income, iDate);
                        if (result) {
                            //incAdd.setText("Update");
                            //createTableInc(true, mNext);
                            Toast toast = Toast.makeText(this, "Income Added Sucessfully", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();
                            mIncome.getText().clear();
                            mDisplayDate.setText("");
                            stk.removeAllViews();
                            createTableInc(true);
                        } else {
                            Toast toast = Toast.makeText(this, "Income for same date has already been added", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(this, "Date Does not exist in the Calendar", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, -690);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -690);
                    toast.show();
                }
            }
            else{
                Toast toast = Toast.makeText(this, "Income cannot be zero", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, -690);
            toast.show();
        }


        //getIncome();

    }

    public void getIncome() {
        FixedCompService srv = new FixedCompServiceImpl();
        createTableInc(true);

    }

    public void createTableInc(boolean isAdd) {

        FixedCompService srv = new FixedCompServiceImpl();
        srv.createTabInc(mContext, stk, isAdd);
    }

    public void createTable(boolean isAdd, Button next) {

        FixedCompService srv = new FixedCompServiceImpl();
        srv.createTab(mContext, stk, isAdd);
    }

    public void nextClick(View v) {

        Intent i = new Intent(IncomeActivity.this, FixedActivity.class);
        startActivity(i);

    }

    public void clickBack(View v) {
        Intent i = new Intent(IncomeActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
