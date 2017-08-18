package com.wangyizhuo.rfidindoorlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.wangyizhuo.rfidindoorlocation.db.Label;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by wangyizhuo on 2017/8/16.
 */

public class OutdoorMapFragment extends Fragment implements View.OnClickListener {
    private int sheetState = BottomSheetBehavior.STATE_HIDDEN;
    private LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView sheetTitle;
    private BottomSheetBehavior behavior;
    private RelativeLayout bottomSheet;
    private boolean isFirstLocate = false;
    private ImageView contentImage;
    private PhotoViewAttacher attacher;
    private ImageView indoorMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //开启定位客户端
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        //设置位置信息变化监听器
        setLocationListener();
        //初始化地图
        SDKInitializer.initialize(getActivity().getApplicationContext());
        //加载碎片界面
        View mView = inflater.inflate(R.layout.fragment_map_out, container, false);

        mapView = (MapView) mView.findViewById(R.id.mapView_out);
        baiduMap = mapView.getMap();
        List<Label> overLayLabelList = new ArrayList<>();
        bottomSheet = (RelativeLayout) mView.findViewById(R.id.sheet_bottom);
        sheetTitle = (TextView) mView.findViewById(R.id.tv_sheet_title);
        behavior = BottomSheetBehavior.from(bottomSheet);
        contentImage = (ImageView) mView.findViewById(R.id.iv_sheet_content);
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                mView.findViewById(R.id.button_float);
        indoorMap = (ImageView) mView.findViewById(R.id.iv_indoor_map);

        //bottomsheet设置
        setBottomSheet();
        //地图设置
        UiSettings uiSettings = baiduMap.getUiSettings();
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);
        mapView.showZoomControls(false);
        //请求位置信息，开启定位服务
        requestLocation();
        //开启我的位置
        baiduMap.setMyLocationEnabled(true);
        //设置标签marker
        overLayLabelList.add(LabelsFragment.labelList.get(0));
        addLabelsOverLay(overLayLabelList);
        //设置标签marker点击事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Label label = (Label) marker.getExtraInfo().get("label");
                if (sheetState == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                //加载marker信息
                Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(contentImage);
                return false;
            }
        });

        return mView;
    }

    //设置标签覆盖物
    private void addLabelsOverLay(List<Label> overLayLabelList) {
        BitmapDescriptor markerIcon = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_marker_location);
        LatLng latLng = null;
        Marker marker;
        MarkerOptions options;

        for(Label label : overLayLabelList){
            //获取经纬度
            latLng = new LatLng(label.getLatitude(), label.getLongitude());
            //设置marker
            options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(markerIcon);//设置图标样式
            //添加marker
            marker = (Marker) baiduMap.addOverlay(options);
            //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
            Bundle bundle = new Bundle();
            bundle.putSerializable("label", label);
            marker.setExtraInfo(bundle);
        }
        //将地图显示在最后一个marker的位置
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.setMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(5f);
        baiduMap.setMapStatus(update);
    }

    //请求位置信息
    private void requestLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        mLocationClient.setLocOption(option);
        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
    }

    //地图移动到我的位置
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(19f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        //显示我的位置
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData myLocationData = locationBuilder.build();
        baiduMap.setMyLocationData(myLocationData);
    }

    //设置位置信息变化监听器
    private void setLocationListener() {
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                        bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    navigateTo(bdLocation);
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {
            }
        });
    }
    //bottomsheet设置
    public void setBottomSheet() {
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetState = newState;
                if(newState != BottomSheetBehavior.STATE_COLLAPSED &&
                        contentImage.getVisibility() == View.GONE){
                    contentImage.setVisibility(View.VISIBLE);
                }else if (newState == BottomSheetBehavior.STATE_COLLAPSED &&
                        contentImage.getVisibility() == View.VISIBLE){
                    contentImage.setVisibility(View.GONE);
                }else if (newState == BottomSheetBehavior.STATE_HIDDEN &&
                        contentImage.getVisibility() == View.VISIBLE) {
                    contentImage.setVisibility(View.GONE);
                }else if (newState == BottomSheetBehavior.STATE_EXPANDED &&
                        mapView.getVisibility() == View.VISIBLE) {
                    mapView.setVisibility(View.GONE);
                } else if (newState != BottomSheetBehavior.STATE_EXPANDED &&
                        mapView.getVisibility() == View.GONE) {
                    mapView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        sheetTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sheetState == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (sheetState == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //室内图设置
        indoorMap.setOnClickListener(this);
    }

    //各类点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_indoor_map:
                //原图图片查看活动
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //碎片销毁取消定位服务
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }
}
