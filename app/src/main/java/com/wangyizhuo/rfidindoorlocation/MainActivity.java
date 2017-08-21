package com.wangyizhuo.rfidindoorlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wangyizhuo.rfidindoorlocation.db.Label;
import com.wangyizhuo.rfidindoorlocation.util.ActivityCollector;
import com.wangyizhuo.rfidindoorlocation.util.LabelAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    private List<String> permissionList;
    private LabelsFragment mLabelsFragment;
    private OutdoorMapFragment mOutdoorMapFragment;
    private ImageView labelsIcon;
    private ImageView locationIcon;
    private LinearLayout selectedLabelLayout;
    private TextView selectedLabelText;
    public static Label selectedLabel;

    public void setSelectedLabel(Label selectedLabel) {
        MainActivity.selectedLabel = selectedLabel;
        selectedLabelText.setText(selectedLabel.getLabelName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_main);

        CircleImageView circle = (CircleImageView) findViewById(R.id.civ_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        CircleImageView headImage = (CircleImageView) headerView.findViewById(R.id.civ_draw_image);
        TextView drawName = (TextView) headerView.findViewById(R.id.tv_draw_name);
        labelsIcon = (ImageView) findViewById(R.id.iv_labels_icon);
        locationIcon = (ImageView) findViewById(R.id.iv_location_icon);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_labels);
        mLabelsFragment = (LabelsFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_labels);
        selectedLabelLayout = (LinearLayout) headerView.findViewById(R.id.layout_label_selected);
        selectedLabelText = (TextView) headerView.findViewById(R.id.tv_label_selected);

        //加载碎片
        initLablesFragment();
        //动态申请权限
        requestLocationPermissions();
        //切换碎片界面点击事件
        labelsIcon.setOnClickListener(this);
        locationIcon.setOnClickListener(this);
        //点击头像转到账户设置
        headImage.setOnClickListener(this);
        drawName.setOnClickListener(this);
        //点击左上角头像打开抽屉
        circle.setOnClickListener(this);
        //抽屉菜单栏点击事件
        navigationView.setNavigationItemSelectedListener(this);
        selectedLabelLayout.setOnClickListener(this);
    }

    //各类点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_label_selected:
                if (selectedLabel.getLatLng() != null) {
                    OutdoorMapFragment outdoorMapFragment = initOutdoorMapFragment();
                    outdoorMapFragment.zoomToLabelLocation(selectedLabel);
                } else {
                    Toast.makeText(this, "标签位置信息有误", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.civ_bar:
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(Gravity.START);
                }
                break;
            case R.id.iv_labels_icon:

                initLablesFragment();
                break;
            case R.id.iv_location_icon:
                initOutdoorMapFragment();
                break;
            case R.id.civ_draw_image:
            case R.id.tv_draw_name:
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
                break;
        }
    }

    //加载map碎片
    public OutdoorMapFragment initOutdoorMapFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            locationIcon.setBackgroundTintList
                    (getColorStateList(android.R.color.holo_blue_light));
            labelsIcon.setBackgroundTintList
                    (getColorStateList(android.R.color.tertiary_text_dark));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mOutdoorMapFragment == null) {
            mOutdoorMapFragment = new OutdoorMapFragment();
            transaction.add(R.id.container, mOutdoorMapFragment);
        }
        hideFragment(transaction);
        transaction.show(mOutdoorMapFragment);
        transaction.commit();
        return mOutdoorMapFragment;
    }
    //加载labels碎片
    private void initLablesFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            labelsIcon.setBackgroundTintList
                    (getColorStateList(android.R.color.holo_blue_light));
            locationIcon.setBackgroundTintList
                    (getColorStateList(android.R.color.tertiary_text_dark));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mLabelsFragment == null) {
            mLabelsFragment = new LabelsFragment();
            transaction.add(R.id.container, mLabelsFragment);
        }
        hideFragment(transaction);
        transaction.show(mLabelsFragment);
        transaction.commit();
    }
    //隐藏所有碎片
    private void hideFragment(FragmentTransaction transaction) {
        if (mLabelsFragment != null) {
            transaction.hide(mLabelsFragment);
        }
        if (mOutdoorMapFragment != null) {
            transaction.hide(mOutdoorMapFragment);
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //抽屉菜单栏点击事件
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_alarm) {

        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, LabelManagerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //收到动态权限申请请求
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能运行本程序",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    //动态申请权限
    private void requestLocationPermissions() {
        permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.
                    toArray(new String[permissionList.size()]), 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
