package com.fanwe.baimei.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.baimei.adapter.holder.BMDailyTaskViewHolder;
import com.fanwe.baimei.adapter.holder.BMTopListViewHolder;
import com.fanwe.baimei.model.BMDailyTasksListModel;
import com.fanwe.library.adapter.SDRecyclerAdapter;
import com.fanwe.library.adapter.viewholder.SDRecyclerViewHolder;
import com.fanwe.live.R;
import com.fanwe.live.model.App_ContModel;

/**
 * 包名: com.fanwe.baimei.adapter
 * 描述: 每日任务列表适配器
 * 作者: Su
 * 创建时间: 2017/5/25 15:41
 **/
public class BMTopListAdapter extends SDRecyclerAdapter<App_ContModel>
{

    public BMTopListAdapter(Activity activity)
    {
        super(activity);
    }


    @Override
    public SDRecyclerViewHolder<App_ContModel> onCreateVHolder(ViewGroup parent, int viewType)
    {
        return new BMTopListViewHolder(parent, R.layout.bm_view_holder_top_list);
    }

    @Override
    public void onBindData(SDRecyclerViewHolder<App_ContModel> holder, int position, App_ContModel model)
    {
        holder.onBindData(position, model);
    }
}
