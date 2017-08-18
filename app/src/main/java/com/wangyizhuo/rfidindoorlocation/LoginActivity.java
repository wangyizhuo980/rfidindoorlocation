package com.wangyizhuo.rfidindoorlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wangyizhuo.rfidindoorlocation.util.ActivityCollector;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_login);

        ImageView background = (ImageView) findViewById(R.id.iv_login_background);
        Glide.with(this).load(R.drawable.login_background).into(background);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
