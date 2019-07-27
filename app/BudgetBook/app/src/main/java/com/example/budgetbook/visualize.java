package com.example.budgetbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class visualize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);
    }

    public void visualize_fixed(View v){
        Intent i = new Intent(visualize.this, Visualize_Fixed.class);
        startActivity(i);
    }

    public void visualize_unfixed(View v){
        Intent i = new Intent(visualize.this, Visualize_Unfxd.class);
        startActivity(i);
    }
}
