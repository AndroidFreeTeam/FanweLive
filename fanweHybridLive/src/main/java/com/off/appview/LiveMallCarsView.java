package com.off.appview;

import android.app.Activity;
import android.content.Context;
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
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.view.LiveTabUnderline;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 座驾商城
 */
public class LiveMallCarsView extends BaseAppView {

    public LiveMallCarsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMallCarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMallCarsView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.tab_mall_cars)
    private LiveTabUnderline tab_mall_cars;
    @ViewInject(R.id.tab_my_cars)
    private LiveTabUnderline tab_my_cars;

    @ViewInject(R.id.vpg_content)
    private SDViewPager vpg_content;

    private SparseArray<BaseAppView> arrFragment = new SparseArray<>();

    private SDSelectViewManager<LiveTabUnderline> selectViewManager = new SDSelectViewManager<>();

    @Override
    protected int onCreateContentView() {
        return R.layout.view_live_main_mall_car;
    }

    private void init() {
        initSDViewPager();
        initTabs();
    }

    private void initSDViewPager() {
        vpg_content.setOffscreenPageLimit(2);
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
                    view = new LiveMallCarsTabMallView(getActivity());
                    break;
                case 1:
                    view = new LiveMallCarsTabMyView(getActivity());
                    break;
                default:
                    break;
            }
            if (null != view) {
                arrFragment.put(position, view);
            }
            return view;
        }
    }

    private void changeLiveTabUnderline(LiveTabUnderline tabUnderline, String title) {
        tabUnderline.getTextViewTitle().setText(title);
        tabUnderline.configViewUnderline().setWidthNormal(SDViewUtil.dp2px(50)).setWidthSelected(SDViewUtil.dp2px(50));
        tabUnderline.configTextViewTitle().setTextSizeNormal(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_12)).setTextSizeSelected(SDResourcesUtil.getDimensionPixelSize(R.dimen.base_textsize_15));
    }

    private void initTabs() {
        changeLiveTabUnderline(tab_mall_cars, "座驾商城");
        changeLiveTabUnderline(tab_my_cars, "我的座驾");

        LiveTabUnderline[] items = new LiveTabUnderline[]{tab_mall_cars, tab_my_cars};
        selectViewManager.addSelectCallback(new SDSelectManager.SelectCallback<LiveTabUnderline>() {

            @Override
            public void onNormal(int index, LiveTabUnderline item) {
            }

            @Override
            public void onSelected(int index, LiveTabUnderline item) {
                switch (index) {
                    case 0:
                        clickTabCarMall();
                        break;
                    case 1:
                        clickTabMyCar();
                        break;
                    default:
                        break;
                }
            }
        });
        selectViewManager.setItems(items);
        selectViewManager.setSelected(0, true);
    }

    protected void clickTabCarMall() {
        vpg_content.setCurrentItem(0);
    }

    protected void clickTabMyCar() {
        vpg_content.setCurrentItem(1);
    }


}
