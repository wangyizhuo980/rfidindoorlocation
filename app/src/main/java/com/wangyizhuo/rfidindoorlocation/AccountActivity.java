package com.wangyizhuo.rfidindoorlocation;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wangyizhuo.rfidindoorlocation.util.ActivityCollector;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AccountActivity extends SwipeBackActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_account);

        ImageView back = (ImageView) findViewById(R.id.iv_back_account);
        TextView exit = (TextView) findViewById(R.id.tv_exit_account);

        back.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back_account:
                //需要完成保存数据操作
                finish();
                break;
            case R.id.tv_exit_account:
                ActivityCollector.finishAll();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
