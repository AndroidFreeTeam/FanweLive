package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.lib.viewpager.pullcondition.IgnorePullCondition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fanwe.library.view.SDRecyclerView;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabNearbyAdapter_1;
import com.fanwe.live.appview.LiveTabHotHeaderView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.Index_indexActModel;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.LiveBannerModel;
import com.fanwe.live.model.LiveFilterModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;

import java.util.Collections;
import java.util.Comparator;

/**
 * 首页附近直播列表
 */
public class LiveTabHotView extends LiveTabBaseView
{
    public LiveTabHotView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public LiveTabHotView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabHotView(Context context)
    {
        super(context);
        init();
    }

    private SDRecyclerView rv_content;
    private LiveTabHotHeaderView mHeaderView;

    private List<LiveRoomModel> mListModel = new ArrayList<>();
    private LiveTabNearbyAdapter_1 mAdapter;
    /**
     * 请求接口的次数
     */
    private int mRequestCount;

    private void init()
    {
        setContentView(R.layout.view_live_tab_nearby_top);
        rv_content = (SDRecyclerView) findViewById(R.id.rv_content);
        rv_content.setGridVertical(2);
        addHeaderView();
        setAdapter();
        getStateLayout().setAdapter(mAdapter);

        updateParams();

        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper()
        {
            @Override
            public void onRefreshingFromHeader()
            {
                startLoopRunnable();
            }

            @Override
            public void onRefreshingFromFooter()
            {
            }
        });
        startLoopRunnable();
    }
    /**
     * 添加HeaderView
     */
    private void addHeaderView()
    {
        FrameLayout view = (FrameLayout) findViewById(R.id.lth_view);
        mHeaderView = new LiveTabHotHeaderView(getActivity());
        mHeaderView.setBannerItemClickCallback(new SDItemClickCallback<LiveBannerModel>()
        {
            @Override
            public void onItemClick(int position, LiveBannerModel item, View view)
            {
                Intent intent = item.parseType(getActivity());
                if (intent != null)
                {
                    getActivity().startActivity(intent);
                }
            }
        });
        SDViewPager viewPager = getParentViewPager();
        if (viewPager != null)
        {
            viewPager.addPullCondition(new IgnorePullCondition(mHeaderView.getSlidingView()));
        }
        view.addView(mHeaderView);
    }
    private void setAdapter()
    {
        mAdapter = new LiveTabNearbyAdapter_1(new ArrayList<LiveRoomModel>(), getActivity());
        rv_content.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        super.onActivityResumed(activity);
        startLoopRunnable();
    }

    @Override
    protected void onLoopRun()
    {
        requestData();
    }

    /**
     * 请求接口数据
     */
    private void requestData1()
    {
        if (mRequestCount % 5 == 0)
        {
            SDTencentMapManager.getInstance().startLocation(null);
        }

        CommonInterface.requestNewVideo(1, new AppRequestCallback<Index_new_videoActModel>()
        {

            @Override
            protected void onSuccess(final SDResponse resp)
            {
                if (actModel.isOk())
                {
                    synchronized (LiveTabHotView.this)
                    {
                        mListModel = actModel.getList();
                        sortData(mListModel);
                        mAdapter.updateData(mListModel);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });

        mRequestCount++;
    }

    /**
     * 对数据进行排序
     *
     * @param list
     */
    private void sortData(List<LiveRoomModel> list)
    {
        if (SDCollectionUtil.isEmpty(list))
        {
            return;
        }
        if (SDTencentMapManager.getInstance().hasLocationSuccess())
        {
            Collections.sort(list, new Comparator<LiveRoomModel>()
            {
                @Override
                public int compare(LiveRoomModel lhs, LiveRoomModel rhs)
                {
                    double result = lhs.getDistance() - rhs.getDistance();
                    if (result < 0)
                    {
                        return -1;
                    } else if (result == 0)
                    {
                        return 0;
                    } else
                    {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public void scrollToTop()
    {
        rv_content.scrollToStart();
    }

    @Override
    protected void onRoomClosed(final int roomId)
    {
        SDHandlerManager.getBackgroundHandler().post(new SDTaskRunnable<LiveRoomModel>()
        {

            @Override
            public LiveRoomModel onBackground()
            {
                synchronized (LiveTabHotView.this)
                {
                    if (SDCollectionUtil.isEmpty(mListModel))
                    {
                        return null;
                    }
                    Iterator<LiveRoomModel> it = mListModel.iterator();
                    while (it.hasNext())
                    {
                        LiveRoomModel item = it.next();
                        if (roomId == item.getRoom_id())
                        {
                            it.remove();
                            return item;
                        }
                    }
                }
                return null;
            }

            @Override
            public void onMainThread(LiveRoomModel result)
            {
                if (result != null)
                {
                    synchronized (LiveTabHotView.this)
                    {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }



    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private int mSex;
    private String mCity;

    /**
     * 请求热门首页接口
     */
    private void requestData()
    {
        CommonInterface.requestIndex(1, mSex, 0, mCity, new AppRequestCallback<Index_indexActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    mHeaderView.setData(actModel);

                    synchronized (LiveTabHotView.this)
                    {
                        mListModel = actModel.getList();
                        mAdapter.updateData(mListModel);
                    }
                }
            }

            @Override
            protected void onError(SDResponse resp)
            {
                super.onError(resp);
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                getPullToRefreshViewWrapper().stopRefreshing();
                super.onFinish(resp);
            }
        });
    }
    /**
     * 更新接口过滤条件
     */
    private void updateParams()
    {
        LiveFilterModel model = LiveFilterModel.get();
        mSex = model.getSex();
        mCity = model.getCity();
    }
}

