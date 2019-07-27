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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbook.constants.Constants;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.UnFxdService;
import com.example.budgetbook.service.UnFxdServiceImpl;
import com.example.budgetbook.utility.CommonUtility;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnfixedActivity extends AppCompatActivity {

    private Context context;
    private Spinner unfxd_comp;
    private EditText unfxd_amount;
    private EditText comment;
    private Spinner payMode;
    public TableLayout stk;
    private static final String TAG = "UnfixedActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateListener;
    private ImageButton mDatePicker;
    public Constants constants= new Constants();
    public CommonUtility utility = new CommonUtility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfixed);
        context = getApplicationContext();
        unfxd_comp = findViewById(R.id.unfxd_dropdown);
        unfxd_amount = findViewById(R.id.unfxd_amount);
        comment = findViewById(R.id.comment);
        payMode = findViewById(R.id.paymentMode);
        stk = findViewById(R.id.limit_table);
        createTable(true);

        mDisplayDate = findViewById(R.id.UnFxd_date);
        mDatePicker = findViewById(R.id.unfxd_date);
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        UnfixedActivity.this,
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

    }

    public void addExpLimits(View v) {
        float limit=0f;
        boolean result=false;
        UnFxdService ufx = new UnFxdServiceImpl();
        String ufx_amt_val = unfxd_amount.getText().toString();
        if(ufx_amt_val!=null && !ufx_amt_val.isEmpty()) {
            limit = Math.round(Float.parseFloat(ufx_amt_val) * 100.0) / 100.0f;
        }
        String ufx_comp = unfxd_comp.getSelectedItem().toString();
        String ufx_pay = payMode.getSelectedItem().toString();
        String commt = comment.getText().toString();
        String fdate = mDisplayDate.getText().toString();
        FixedVO vo = new FixedVO();
        vo.setPayMode(ufx_pay);
        vo.setPayDate(fdate);
        vo.setComponent(ufx_comp);
        vo.setUnfxd_amt(limit);
        vo.setComment(commt);
        if(limit>0 && ufx_pay!=null && !ufx_pay.isEmpty() && fdate!=null && !fdate.isEmpty() && ufx_comp!=null && !ufx_comp.isEmpty()) {
            String regex = constants.date_reg;
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(fdate);
            if(matcher.matches()) {
                if(utility.isDateValid(fdate)) {
                result = ufx.addUnfxdData(vo, context, stk);
                if(result){
                    unfxd_amount.getText().clear();
                    comment.getText().clear();
                    mDisplayDate.setText("");
                    stk.removeAllViews();
                    createTable(true);
                }
                else{
                    Toast toast = Toast.makeText(this,"Expenditure already added", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,-690);
                    toast.show();
                }
            }
            else{
                Toast toast = Toast.makeText(this, "Date Does not exist in the Calendar", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }
            }
            else{
                Toast toast = Toast.makeText(this,"Invalid Date", Toast.LENGTH_SHORT);
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

    public void clickBack(View v){
        Intent i = new Intent(UnfixedActivity.this, DashboardActivity.class);
        startActivity(i);
    }

    public void createTable(boolean isAdd){

        UnFxdService ufx = new UnFxdServiceImpl();
        ufx.createTab(context,stk,isAdd);
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
