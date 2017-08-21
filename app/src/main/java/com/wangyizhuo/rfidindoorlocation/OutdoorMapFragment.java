package com.wangyizhuo.rfidindoorlocation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.wangyizhuo.rfidindoorlocation.db.Label;
import com.wangyizhuo.rfidindoorlocation.db.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/*
 * Created by wangyizhuo on 2017/8/16.
 */

public class OutdoorMapFragment extends Fragment implements View.OnClickListener {
    public static Map map1;
    private BDLocation mLocation;
    private int sheetState = BottomSheetBehavior.STATE_HIDDEN;
    private LocationClient mLocationClient;
    private MapView mapView;
    private BaiduMap baiduMap;
    private TextView sheetTitle;
    private BottomSheetBehavior behavior;
    private RelativeLayout bottomSheet;
    private boolean isFirstLocate = true;
    private ImageView contentImage;
    private PhotoViewAttacher attacher;
    private ImageView indoorMap;
    private FrameLayout mapContainer;
    private LinearLayout pointView;

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
        FloatingActionButton zoomMyLocation = (FloatingActionButton)
                mView.findViewById(R.id.button_float_mlocation);
        indoorMap = (ImageView) mView.findViewById(R.id.iv_map_background);
        TextView labelManager = (TextView) mView.findViewById(R.id.tv_sheet_manager);
        mapContainer = (FrameLayout) mView.findViewById(R.id.container_indoormap);

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
        //设置各项点击事件
        zoomMyLocation.setOnClickListener(this);
        labelManager.setOnClickListener(this);
        //设置标签marker点击事件
        setMarkerClick();

        return mView;
    }

    //各类点击事件
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_map_background:
                //原图图片查看活动
                intent = new Intent(getActivity(), ImageBrowseActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_sheet_manager:
                intent = new Intent(getActivity(), LabelManagerActivity.class);
                startActivity(intent);
                break;
            case R.id.button_float_mlocation:
                zoomToMyLocation();
        }
    }

    private void setMarkerClick() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Label label = (Label) marker.getExtraInfo().get("label");
                ((MainActivity) getActivity()).setSelectedLabel(label);
                if (sheetState == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                //zoom
                zoomToLabelLocation(label);
                //加载marker对应label图标
                Glide.with(getActivity()).load(R.mipmap.ic_launcher_round).into(contentImage);
                return false;
            }
        });
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
            latLng = label.getLatLng();
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

    //我的位置发生变化时
    private void navigate() {
        if (isFirstLocate) {
            zoomToLabelLocation(MainActivity.selectedLabel);
            isFirstLocate = false;
        }
        //显示我的位置
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(mLocation.getLatitude());
        locationBuilder.longitude(mLocation.getLongitude());
        MyLocationData myLocationData = locationBuilder.build();
        baiduMap.setMyLocationData(myLocationData);
    }

    //设置我的位置信息变化监听器
    private void setLocationListener() {
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation ||
                        bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    mLocation = bdLocation;
                    navigate();
                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {
            }
        });
    }
    //zoom到我的位置
    private void zoomToMyLocation() {
        LatLng ll = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(17);
        baiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(update);
    }
    //zoom到标签的位置
    public void zoomToLabelLocation(Label label) {
        if (label.getLatLng() != null && baiduMap != null) {
            MapStatusUpdate update = MapStatusUpdateFactory.zoomTo(17);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.newLatLng(label.getLatLng());
            baiduMap.animateMapStatus(update);
        }
    }

    //bottomsheet设置
    public void setBottomSheet() {
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                sheetState = newState;
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

        //室内图点击设置
        indoorMap.setOnClickListener(this);
        //室内图加载
        map1 = new Map();
        map1.setStartLatLng(32.117970, 118.937945);
        map1.setEndLatLng(32.117825, 118.938187);
        map1.setSrc(R.drawable.map_1);
        indoorMap.setImageResource(map1.getSrc());
        //室内地图标签位置点加载
        pointView = (LinearLayout) LayoutInflater.from(getContext()).
                inflate(R.layout.point_layout, mapContainer, false);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                pointView.getLayoutParams();
            //获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
            //获取图片实际高宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(getResources(), R.drawable.map_1, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        //图片显示高度为
        int imageViewHeight = (int) (screenWidth * ( (double) imageHeight / imageWidth));
             //获取宽度比例
        double widthProportion = (MainActivity.selectedLabel.getLatLng().longitude
                - map1.getStartLatLng().longitude ) / ( map1.getEndLatLng().longitude
                - map1.getStartLatLng().longitude);
            //获取高度比例
        double heightProportion = ( map1.getStartLatLng().latitude
                - MainActivity.selectedLabel.getLatLng().latitude) /
                ( map1.getStartLatLng().latitude - map1.getEndLatLng().latitude);
            //计算出点相对于地图左边界与上边界距离
        params.leftMargin = (int) (screenWidth * widthProportion);
        params.topMargin = (int) (imageViewHeight * heightProportion);
        mapContainer.addView(pointView, params);
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
