package com.fanwe.o2o.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.service.AppUpgradeHelper;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.view.SDTabMenu;
import com.fanwe.library.view.select.SDSelectViewManager;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveCreateRoomActivity;
import com.fanwe.live.activity.LiveCreaterAgreementActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.live.event.EImOnForceOffline;
import com.fanwe.live.fragment.LiveMainMeFragment;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.utils.LiveVideoChecker;
import com.fanwe.o2o.event.EO2ORefreshH5HomePage;
import com.fanwe.o2o.event.EO2OShoppingLiveMainExist;
import com.fanwe.o2o.fragment.O2OShoppingLiveImFragment;
import com.fanwe.o2o.fragment.O2OShoppingLiveTabLiveFragment;
import com.sunday.eventbus.SDEventManager;

import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2016/10/31.
 */

public class O2OShoppingLiveMainActivity extends BaseActivity
{
    @ViewInject(R.id.view_tab_1)
    private SDTabMenu view_tab_1;

    @ViewInject(R.id.view_tab_2)
    private SDTabMenu view_tab_2;

    @ViewInject(R.id.view_tab_3)
    private SDTabMenu view_tab_3;

    @ViewInject(R.id.view_tab_4)
    private SDTabMenu view_tab_4;

    @ViewInject(R.id.iv_tab_create_live)
    private View view_tab_create_live;

    private SDSelectViewManager<SDTabMenu> selectViewManager = new SDSelectViewManager<SDTabMenu>();

    @Override
    protected int onCreateContentView()
    {
        return R.layout.o2o_shopping_act_live_main;
    }

    @Override
    protected void init(Bundle savedInstanceState)
    {
        super.init(savedInstanceState);
        mIsExitApp = false;
        checkUpdate();

        AppRuntimeWorker.startContext();
        CommonInterface.requestUser_apns(null);
        CommonInterface.requestMyUserInfo(null);

        LiveVideoChecker checker = new LiveVideoChecker(this);
        CharSequence copyContent = SDOtherUtil.pasteText();
        checker.check(String.valueOf(copyContent));

        initTabs();
    }

    private void checkUpdate()
    {
        new AppUpgradeHelper(this).check(0);
    }

    private void initTabs()
    {
        view_tab_create_live.setOnClickListener(this);

        view_tab_1.setTextTitle("??????");
        view_tab_2.setTextTitle("??????");
        view_tab_3.setTextTitle("??????");
        view_tab_4.setTextTitle("??????");

        view_tab_1.getViewConfig(view_tab_1.mIvTitle).setImageNormalResId(R.drawable.o2o_shopping_ic_tab_h5_normal).setImageSelectedResId(R.drawable.o2o_shopping_ic_tab_h5_selected);
        view_tab_2.getViewConfig(view_tab_2.mIvTitle).setImageNormalResId(R.drawable.o2o_shopping_ic_tab_video_normal).setImageSelectedResId(R.drawable.o2o_shopping_ic_tab_video_selected);
        view_tab_3.getViewConfig(view_tab_3.mIvTitle).setImageNormalResId(R.drawable.o2o_shopping_ic_tab_im_normal).setImageSelectedResId(R.drawable.o2o_shopping_ic_tab_im_selected);
        view_tab_4.getViewConfig(view_tab_4.mIvTitle).setImageNormalResId(R.drawable.o2o_shopping_ic_tab_me_normal).setImageSelectedResId(R.drawable.o2o_shopping_ic_tab_me_selected);

        view_tab_1.getViewConfig(view_tab_1.mTvTitle).setTextColorNormalResId(R.color.text_home_menu_normal)
                .setTextColorSelectedResId(R.color.text_home_menu_selected);
        view_tab_2.getViewConfig(view_tab_2.mTvTitle).setTextColorNormalResId(R.color.text_home_menu_normal)
                .setTextColorSelectedResId(R.color.text_home_menu_selected);
        view_tab_3.getViewConfig(view_tab_3.mTvTitle).setTextColorNormalResId(R.color.text_home_menu_normal)
                .setTextColorSelectedResId(R.color.text_home_menu_selected);
        view_tab_4.getViewConfig(view_tab_4.mTvTitle).setTextColorNormalResId(R.color.text_home_menu_normal)
                .setTextColorSelectedResId(R.color.text_home_menu_selected);

        selectViewManager.addSelectCallback(new SDSelectViewManager.SelectCallback<SDTabMenu>()
        {

            @Override
            public void onNormal(int index, SDTabMenu item)
            {
            }

            @Override
            public void onSelected(int index, SDTabMenu item)
            {
                switch (index)
                {
                    case 0:
                        clickShoppingClose();
                        break;
                    case 1:
                        clickTabLive();
                        break;
                    case 2:
                        clickTabImInfo();
                        break;
                    case 3:
                        clickTabMe();
                        break;
                    default:
                        break;
                }
            }
        });

        SDTabMenu[] items = new SDTabMenu[]{view_tab_1, view_tab_2, view_tab_3, view_tab_4};
        selectViewManager.setItems(items);

        selectViewManager.performClick(1);
    }

    @Override
    public void onClick(View v)
    {
        if (v == view_tab_create_live)
        {
            clickTabCreateLive();
        }
        super.onClick(v);
    }

    protected void clickTabLive()
    {
        getSDFragmentManager().toggle(R.id.fl_main_content, null, O2OShoppingLiveTabLiveFragment.class);
    }

    protected void clickTabImInfo()
    {
        getSDFragmentManager().toggle(R.id.fl_main_content, null, O2OShoppingLiveImFragment.class);
    }

    private void clickShoppingClose()
    {
        EO2ORefreshH5HomePage event = new EO2ORefreshH5HomePage();
        SDEventManager.post(event);
        finish();
    }

    protected void clickTabMe()
    {
        getSDFragmentManager().toggle(R.id.fl_main_content, null, LiveMainMeFragment.class);
    }

    private void clickTabCreateLive()
    {
        if (AppRuntimeWorker.isLogin(this))
        {
            final UserModel userModel = UserModelDao.query();
            if (userModel.getIs_agree() == 1)
            {
                Intent intent = new Intent(this, LiveCreateRoomActivity.class);
                startActivity(intent);
            } else
            {
                Intent intent = new Intent(this, LiveCreaterAgreementActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    public void onEventMainThread(EImOnForceOffline event)
    {
        AppDialogConfirm dialog = new AppDialogConfirm(this);
        dialog.setTextContent("???????????????????????????????????????");
        dialog.setTextCancel("??????");
        dialog.setTextConfirm("????????????");
        dialog.setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
                EO2OShoppingLiveMainExist event = new EO2OShoppingLiveMainExist();
                SDEventManager.post(event);
                finish();
            }

            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                AppRuntimeWorker.startContext();
            }
        }).show();
    }
}
