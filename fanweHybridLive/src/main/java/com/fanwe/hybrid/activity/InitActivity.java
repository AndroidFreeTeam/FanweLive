package com.fanwe.hybrid.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.event.ERetryInitSuccess;
import com.fanwe.lib.dialog.ISDDialogConfirm;
import com.fanwe.lib.dialog.impl.SDDialogBase;
import com.fanwe.lib.dialog.impl.SDDialogConfirm;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.IMHelper;
import com.fanwe.live.R;
import com.fanwe.live.business.InitBusiness;
import com.fanwe.live.dialog.common.AppDialogConfirm;
import com.fanwe.xianrou.activity.QKMySmallVideoActivity;
import com.tencent.bugly.imsdk.crashreport.CrashReport;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-16 下午4:39:42 类说明 启动页
 */
public class InitActivity extends BaseActivity
{
    private InitBusiness mInitBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setFullScreen(true);
        setContentView(R.layout.act_init);
        if(ContextCompat.checkSelfPermission(InitActivity.this,Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(InitActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(InitActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(InitActivity.this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||  ContextCompat.checkSelfPermission(InitActivity.this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ){
            AppDialogConfirm dialogConfirm = new AppDialogConfirm(InitActivity.this);
            dialogConfirm.setCanceledOnTouchOutside(false);
            dialogConfirm.setCancelable(false);
            dialogConfirm.setTextContent("APP需要获取权限才能正常运行,请确认");
            dialogConfirm.setTextCancel("退出").setTextConfirm("确定").setCallback(new ISDDialogConfirm.Callback()
            {
                @Override
                public void onClickCancel(View v, SDDialogBase dialog)
                {
                    App.getApplication().exitApp(false);
                }
                @Override
                public void onClickConfirm(View v, SDDialogBase dialog)
                {
                    ActivityCompat.requestPermissions(InitActivity.this,
                            new String[]{
                                    Manifest.permission.READ_PHONE_STATE,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO
                            }, 100);
                }
            }).show();
        }else{
            init();
        }
    }

    private void init(){
        mInitBusiness = new InitBusiness();
        mInitBusiness.init(InitActivity.this);
    }
    public void onEventMainThread(ERetryInitSuccess event)
    {
        InitBusiness.dealInitLaunchBusiness(this);
    }

    @Override
    protected void onDestroy()
    {
        if(null != mInitBusiness){
            mInitBusiness.onDestroy();
            mInitBusiness = null;
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(permissions.length != 5 || grantResults.length != 5){
                showRequestPermissionFailDialog();
            }
            for (int result: grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED){
                    showRequestPermissionFailDialog();
                    return;
                }
            }
            init();
        }
    }

    private void showRequestPermissionFailDialog(){
        AppDialogConfirm dialogConfirm = new AppDialogConfirm(InitActivity.this);
        dialogConfirm.setCanceledOnTouchOutside(false);
        dialogConfirm.setCancelable(false);
        dialogConfirm.setTextContent("由于您没有授予权限,APP即将退出");
        dialogConfirm.setTextCancel("").setTextConfirm("确定").setCallback(new ISDDialogConfirm.Callback()
        {
            @Override
            public void onClickCancel(View v, SDDialogBase dialog)
            {
            }
            @Override
            public void onClickConfirm(View v, SDDialogBase dialog)
            {
                App.getApplication().exitApp(false);
            }
        }).show();
    }
}
