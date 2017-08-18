package com.wangyizhuo.rfidindoorlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangyizhuo.rfidindoorlocation.util.ActivityCollector;

public class LabelManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_label_manager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
