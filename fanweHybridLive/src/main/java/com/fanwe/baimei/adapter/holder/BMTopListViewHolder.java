package com.fanwe.baimei.adapter.holder;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.fanwe.baimei.model.BMDailyTasksListModel;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.utils.GlideUtil;

/**
 * 包名: com.fanwe.baimei.adapter.holder
 * 描述:
 * 作者: Su
 * 创建时间: 2017/5/25 15:23
 **/
public class BMTopListViewHolder extends SDRecyclerViewHolder<App_ContModel>
{
    private TextView tvNickName;
    private TextView tvNum;

    public BMTopListViewHolder(ViewGroup parent, int layoutId)
    {
        super(parent, layoutId);
        tvNickName = (TextView)findViewById(R.id.tv_nick_name);
        tvNum = (TextView)findViewById(R.id.tv_num);
    }

    @Override
    public void onBindData(final int position, final App_ContModel model)
    {
        if(null != model){
            tvNickName.setText( "No."+(position + 1) + " " + model.getNick_name());
            tvNum.setText(model.getNum() + "");
        }
    }

}
