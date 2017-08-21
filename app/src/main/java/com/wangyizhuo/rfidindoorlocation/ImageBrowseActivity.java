package com.wangyizhuo.rfidindoorlocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.wangyizhuo.rfidindoorlocation.util.ActivityCollector;

public class ImageBrowseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_image_browse);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        PhotoView photoView = (PhotoView) findViewById(R.id.photo_view);
        PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        //加载图片
//        switch (type) {
//
//        }
        Glide.with(this).load(OutdoorMapFragment.map1.getSrc()).into(photoView);
        //单击退出
        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
