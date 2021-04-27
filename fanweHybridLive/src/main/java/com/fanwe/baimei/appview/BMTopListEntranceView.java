package com.fanwe.baimei.appview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.fanwe.baimei.adapter.BMTopListAdapter;
import com.fanwe.baimei.dialog.BMDailyTasksDialog;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_ContActModel;
import com.fanwe.live.model.App_ContModel;
import com.fanwe.live.model.App_get_videoActModel;
import com.fanwe.xianrou.util.TimeUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 包名: com.fanwe.baimei.appview
 * 描述: 每日任务列表入口
 * 作者: Su
 * 创建时间: 2017/5/31 17:04
 **/
public class BMTopListEntranceView extends BaseAppView
{
    private App_get_videoActModel mRoomInfo;
    private Handler mDelayHandler;
    private Runnable mAlarmRunnable;
    private BMTopListAdapter mAdapter;
    private SDRecyclerView mRecyclerView;


    public BMTopListEntranceView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initBMTopListEntranceView(context);
    }

    public BMTopListEntranceView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initBMTopListEntranceView(context);
    }

    public BMTopListEntranceView(Context context)
    {
        super(context);
        initBMTopListEntranceView(context);
    }

    private void initBMTopListEntranceView(Context context)
    {
        setContentView(R.layout.bm_view_top_list_entrance);
        mAdapter = new BMTopListAdapter(getActivity());
        mRecyclerView = findViewById(R.id.rv_top_list);
        mRecyclerView.setAdapter(mAdapter);
        startAlarm();
    }
    /**
     * 开始提醒动画
     */
    public void startAlarm()
    {
        startAlarm(0);
    }

    /**
     * 开始提醒动画
     */
    public void startAlarm(long delayMills)
    {
        getDelayHandler().postDelayed(getAlarmRunnable(),delayMills);
    }
    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        getDelayHandler().removeCallbacks(getAlarmRunnable());
    }

    private Handler getDelayHandler()
    {
        if (mDelayHandler == null)
        {
            mDelayHandler = new Handler(Looper.getMainLooper());
        }
        return mDelayHandler;
    }

    private Runnable getAlarmRunnable()
    {
        if (mAlarmRunnable == null)
        {
            mAlarmRunnable = new Runnable()
            {
                @Override
                public void run()
                {
                    if(null != mRoomInfo && isVisible()){
                        CommonInterface.requestRoomCont(mRoomInfo.getRoom_id(), new AppRequestCallback<App_ContActModel>() {
                            @Override
                            protected void onSuccess(SDResponse sdResponse) {
                                if(actModel.isOk()){
                                    List<App_ContModel> list = actModel.getList();
                                    if(null==list){
                                        list = new ArrayList<>();
                                    }
                                    if(list.size()>1){
                                        Collections.sort(list, new Comparator<App_ContModel>() {
                                            @Override
                                            public int compare(App_ContModel o1, App_ContModel o2) {
                                                return o2.getNum() - o1.getNum();
                                            }
                                        });
                                    }
                                    mAdapter.setData(actModel.getList());
                                    mAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    startAlarm(5000);
                }
            };
        }
        return mAlarmRunnable;
    }

    public void setRoomInfo(App_get_videoActModel roomInfo) {
        this.mRoomInfo = roomInfo;
    }
}
