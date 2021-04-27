package com.off;

import android.support.v7.widget.AppCompatRadioButton;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.live.R;
import com.off.bean.CarsBean;

import java.util.List;

public class MyCarAdapter extends BaseQuickAdapter<CarsBean, BaseViewHolder> {

    public interface ClickInterface {
        void play(int position);
        void select(int position);
        void checked(int position, boolean isChecked);
    }

    private ClickInterface clickInterface;

    public MyCarAdapter(List<CarsBean> data, ClickInterface clickInterface) {
        super(R.layout.item_my_car, data);
        this.clickInterface = clickInterface;
    }

    @Override
    protected void convert(final BaseViewHolder holder, CarsBean item) {
        holder.setText(R.id.tvName, item.getName());
        long time = (SDTypeParseUtil.getLong(item.getData()) * 86400 + SDTypeParseUtil.getLong(item.getAdd_time())) - System.currentTimeMillis() / 1000;
        String tips = "1分钟内";
        int minutes = (int)time / 60;
        int hour = minutes / 60;
        int day = hour / 24;
        if(minutes > 0){
            tips = minutes + "分钟后";
        }
        if(hour > 0){
            tips = hour + "小时后";
        }
        if(day > 0){
            tips = day + "天后";
        }
        holder.setText(R.id.tvPrice, tips + "到期");

        ImageView ivCar = holder.getView(R.id.ivCar);
        Glide.with(mContext)
                .load(item.getIcon())
                .into(ivCar);

        ImageView ivPlay = holder.getView(R.id.ivPlay);
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInterface != null){
                    clickInterface.play(holder.getLayoutPosition());
                }
            }
        });
        AppCompatRadioButton rbDef = holder.getView(R.id.rbDef);
        if (item.getIs_def() == 1){
            rbDef.setChecked(true);
        } else {
            rbDef.setChecked(false);
        }

        rbDef.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (clickInterface != null){
                    clickInterface.checked(holder.getLayoutPosition(),isChecked);
                }
            }
        });

    }
}
