package com.clara.bismark439.ratemyhangar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class act_popup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_popup);

    }
public void onButton1Click(View v){
    finish();
}
}
