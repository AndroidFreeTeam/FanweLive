package com.off;

import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.fanwe.live.R;
import com.off.bean.CarsBean;

import java.util.List;

public class CarMallAdapter extends BaseQuickAdapter<CarsBean, BaseViewHolder> {

    public interface ClickInterface {
        void play(int position);
        void buy(int position);
    }

    private ClickInterface clickInterface;

    public CarMallAdapter(List<CarsBean> data, ClickInterface clickInterface) {
        super(R.layout.item_mall_car, data);
        this.clickInterface = clickInterface;
    }

    @Override
    protected void convert(final BaseViewHolder holder, CarsBean item) {
        holder.setText(R.id.tvName, item.getName());
        holder.setText(R.id.tvPrice, item.getDiamonds() + "钻石/" + item.getData() + "天");

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

        AppCompatButton btnBuy = holder.getView(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickInterface != null){
                    clickInterface.buy(holder.getLayoutPosition());
                }
            }
        });
    }
}
