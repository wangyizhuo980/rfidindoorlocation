package com.wangyizhuo.rfidindoorlocation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wangyizhuo.rfidindoorlocation.db.Label;
import com.wangyizhuo.rfidindoorlocation.util.LabelAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangyizhuo on 2017/8/14.
 */

public class LabelsFragment extends Fragment {
    public static List<Label> labelList;

    private View mView;
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_labels, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.rv_labels);
        swipeRefresh = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_refresh);

        //测试初始化labelList
        labelList = initLabelList();
        MainActivity.selectedLabel = labelList.get(0);
        //recyclerview填充数据
        LabelAdapter adapter = new LabelAdapter(labelList);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        //设置swipeview刷新事件
        setSwipteView();

        return mView;
    }

    //测试初始化标签信息
    private List<Label> initLabelList() {
        List<Label> labelList = new ArrayList<>();
        for (int i=0; i<20; i++) {
            Label label = new Label();
            label.setLabelName("标签" + (i+1));
            label.setImageSrc(R.mipmap.ic_launcher_round);
            labelList.add(label);
        }
        labelList.get(0).setLatLng(32.117970, 118.937945);
        return labelList;
    }

    //设置swipteview刷新事件
    private void setSwipteView() {
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefresh.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
