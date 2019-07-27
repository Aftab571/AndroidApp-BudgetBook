package com.example.budgetbook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.budgetbook.dao.PinDao;
import com.example.budgetbook.dao.PinDaoImpl;
import com.example.budgetbook.model.FixedVO;
import com.example.budgetbook.service.PinServImpl;
import com.example.budgetbook.service.PinService;
import com.goodiebag.pinview.Pinview;

public class ResetActivity extends AppCompatActivity {

    private Context mcontext;
    private Pinview old_pin;
    private Pinview new_pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        mcontext= getApplicationContext();
        old_pin= findViewById(R.id.old_pin);
        new_pin= findViewById(R.id.new_pin);
    }


    public void reset_pin(View v){
        if(old_pin!=null && !old_pin.getValue().isEmpty() && old_pin.getValue().length()==4 && new_pin!=null && !new_pin.getValue().isEmpty() && new_pin.getValue().length()==4){
            int oldPin = Integer.parseInt(old_pin.getValue());
            int newPin = Integer.parseInt(new_pin.getValue());
            if(oldPin!=newPin) {
                PinService psrv = new PinServImpl();
                PinDao dao = new PinDaoImpl(mcontext);
                FixedVO vo = psrv.getPin(mcontext);
                if (vo.getPin() == oldPin) {
                    long isDelete = dao.deletePin(mcontext, oldPin);
                    if (isDelete > 0) {
                        boolean isInsert = psrv.insertPin(mcontext, newPin);
                        if (isInsert) {
                            old_pin.setValue("");
                            new_pin.setValue("");
                            Toast toast = Toast.makeText(this, "PIN was Reset Sucessfully", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();
                        } else {
                            Toast toast = Toast.makeText(this, "System error occurred", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, -690);
                            toast.show();
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(this, "Old PIN is Invalid. Enter your current PIN", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -690);
                    toast.show();
                }
            }
            else{
                Toast toast = Toast.makeText(this, "Both current and new PIN are same", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, -690);
                toast.show();
            }

        }

        else{
            Toast toast = Toast.makeText(this, "Enter All mandatory fields", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, -690);
            toast.show();
        }

    }

    public void clickBack(View v) {
        Intent i = new Intent(ResetActivity.this, DashboardActivity.class);
        startActivity(i);
    }
}
