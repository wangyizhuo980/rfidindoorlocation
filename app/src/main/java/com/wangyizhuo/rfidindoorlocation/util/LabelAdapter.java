package com.wangyizhuo.rfidindoorlocation.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wangyizhuo.rfidindoorlocation.MainActivity;
import com.wangyizhuo.rfidindoorlocation.OutdoorMapFragment;
import com.wangyizhuo.rfidindoorlocation.R;
import com.wangyizhuo.rfidindoorlocation.db.Label;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wangyizhuo on 2017/8/14.
 */

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder> {
    private List<Label> mLabelList;
    private Context mContext;
    private MainActivity main;

    public LabelAdapter(List<Label> labelList) {
        this.mLabelList = labelList;
    }

    public void setActivity(MainActivity main) {
        this.main = main;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time;
        private TextView name;
        private TextView info;
        private ConstraintLayout layout;
        private CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_account_name);
            info = (TextView) itemView.findViewById(R.id.tv_account_code);
            time = (TextView) itemView.findViewById(R.id.tv_label_time);
            image = (CircleImageView) itemView.findViewById(R.id.civ_account);
            layout = (ConstraintLayout) itemView.findViewById(R.id.layout_label_item);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Label label = mLabelList.get(position);
        holder.name.setText(label.getLabelName());
        holder.info.setText("这是标签的描述信息");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        holder.time.setText(formatter.format(label.getDate()));
        if (label.getImageSrc() != 0) {
            Glide.with(mContext).load(label.getImageSrc()).into(holder.image);
        } else if (label.getImageURL() != null) {
            Glide.with(mContext).load(label.getImageURL()).into(holder.image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_label, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Label label = mLabelList.get(position);
                main.setSelectedLabel(label);
                if (label.getLatLng() != null) {
                    OutdoorMapFragment outdoorMapFragment = main.initOutdoorMapFragment();
                    outdoorMapFragment.zoomToLabelLocation(label);
                } else {
                    Toast.makeText(main, "标签位置信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return holder;
    }

    @Override
    public int getItemCount() {
        return mLabelList.size();
    }

}
