package com.off.appview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.lib.viewpager.SDViewPager;
import com.fanwe.library.adapter.SDPagerAdapter;
import com.fanwe.library.common.SDSelectManager;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveChatC2CActivity;
import com.fanwe.live.activity.LiveSearchUserActivity;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.appview.main.LiveMainRechargeView;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页 -- 商城
 */
public class LiveMainMallView extends BaseAppView {
    public LiveMainMallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMainMallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMainMallView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.tab_mall_diamond)
    private LiveTabUnderline tab_mall_diamond;
    @ViewInject(R.id.tab_mall_cars)
    private LiveTabUnderline tab_mall_cars;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<BaseAppView> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> selectViewManager = new SDSelectViewManager<>();

    private void init() {
        setContentView(R.layout.view_live_main_mall);
        initSDViewPager();
        initTabs();
    }

    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(3);
        List<String> listModel = new ArrayList<>();
        listModel.add("");
        listModel.add("");
        vpg_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (selectViewManager.getSelectedIndex() != position) {
                    selectViewManager.setSelected(position, true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        vpg_content.setAdapter(new LivePagerAdapter(listModel, getActivity()));

    }

    private class LivePagerAdapter extends SDPagerAdapter<String> {

        public LivePagerAdapter(List<String> listModel, Activity activity) {
            super(listModel, activity);
        }

        @Override
        public View getView(ViewGroup viewGroup, int position) {
            BaseAppView view = null;
            switch (position) {
                case 0:
                    view = new LiveMainRechargeView(getActivity());
                    break;
                case 1:
                    view = new LiveMallCarsView(getActivity());
                    break;
                default:
                    break;
            }
            if (view != null) {
                arrFragment.put(position, view);
            }
            return view;
        }

    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title) {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_16));
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(65)).setWidthSelected(SDViewUtil.dp2px(75));
    }

    private void initTabs() {
        changeLiveTabUnderline(tab_mall_diamond, SDResourcesUtil.getString(R.string.live_recharge_exchange));
        changeLiveTabUnderline(tab_mall_cars, SDResourcesUtil.getString(R.string.live_recharge_car));

        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_mall_diamond, tab_mall_cars};

        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>() {

            @Override
            public void onNormal(int index, LiveTabUnderline item) {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item) {
                switch (index) {
                    case 0:
                        clickTabDiamond();
                        break;
                    case 1:
                        clickTabCar();
                        break;
                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);

    }

    protected void clickTabDiamond() {
        vpg_content.setCurrentItem(0);
    }

    protected void clickTabCar() {
        vpg_content.setCurrentItem(1);
    }
}
