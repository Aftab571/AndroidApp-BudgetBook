package com.example.budgetbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetbook.dao.DataBaseHandlerImpl;
import com.example.budgetbook.dao.FixedCompDaoHandler;
import com.example.budgetbook.dao.LimitDao;
import com.example.budgetbook.dao.LimitDaoImpl;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.FixedCompService;
import com.example.budgetbook.service.FixedCompServiceImpl;
import com.example.budgetbook.service.PinServImpl;
import com.example.budgetbook.service.PinService;
import com.goodiebag.pinview.Pinview;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private TextView mcreate_pin;
    private TextView menter_pin;
    private Context mcontext;
    private Pinview pin_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mcontext = getApplicationContext();
        mcreate_pin = findViewById(R.id.create_pin);
        menter_pin = findViewById(R.id.enter_pin);
        pin_data= findViewById(R.id.pinData);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getPin();


    }

    public void getPin(){
        PinService psrv = new PinServImpl();
        FixedVO pin = psrv.getPin(mcontext);
        if(pin.getPin()<=0){
            menter_pin.setVisibility(View.INVISIBLE);
        }
        else{
            mcreate_pin.setVisibility(View.INVISIBLE);
        }

    }

    public  void checkPin(View v){
        PinService psrv = new PinServImpl();
        int entered_pin = Integer.parseInt(pin_data.getValue());
        FixedVO pinData=psrv.getPin(mcontext);
        FixedCompDaoHandler fx = new DataBaseHandlerImpl(mcontext);
        LimitDao lim = new LimitDaoImpl(mcontext);
        List<FixedVO> fx_list = fx.getFxdTableData();
        List<FixedVO> lim_list = lim.getLimTableData();
        List<FixedVO> inc_list = fx.getIncome(mcontext);

        if(pinData.getPin()<=0){
            boolean result=psrv.insertPin(mcontext,entered_pin);
            if(result){

                if((fx_list!=null && fx_list.size()>0) && (lim_list!=null && lim_list.size()>0) && (inc_list!=null && inc_list.size()>0)){
                    Intent i = new Intent(RegisterActivity.this,DashboardActivity.class);
                    startActivity(i);
                }
                else if((inc_list!=null && inc_list.isEmpty())){
                    Intent i = new Intent(RegisterActivity.this,IncomeActivity.class);
                    startActivity(i);
                }
                else if(fx_list!=null && fx_list.isEmpty()){
                    Intent i = new Intent(RegisterActivity.this,FixedActivity.class);
                    startActivity(i);
                }
                else if(lim_list!=null && lim_list.isEmpty()){
                    Intent i = new Intent(RegisterActivity.this,LimitsActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(RegisterActivity.this,IncomeActivity.class);
                    startActivity(i);
                }


            } else{
                Toast.makeText(RegisterActivity.this,"System Error",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            if(pinData.getPin()==entered_pin){

                if((fx_list!=null && fx_list.size()>0) && (lim_list!=null && lim_list.size()>0) && (inc_list!=null && inc_list.size()>0)){
                    Intent i = new Intent(RegisterActivity.this,DashboardActivity.class);
                    startActivity(i);
                }
                else if((inc_list!=null && inc_list.isEmpty())){
                    Intent i = new Intent(RegisterActivity.this,IncomeActivity.class);
                    startActivity(i);
                }
                else if(fx_list!=null && fx_list.isEmpty()){
                    Intent i = new Intent(RegisterActivity.this,FixedActivity.class);
                    startActivity(i);
                }
                else if(lim_list!=null && lim_list.isEmpty()){
                    Intent i = new Intent(RegisterActivity.this,LimitsActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(RegisterActivity.this,IncomeActivity.class);
                    startActivity(i);
                }

            }
            else{
                Toast.makeText(RegisterActivity.this,"Invalid Pin",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
