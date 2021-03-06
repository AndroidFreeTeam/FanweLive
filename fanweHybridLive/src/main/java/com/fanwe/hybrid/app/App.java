package com.fanwe.hybrid.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.hybrid.constant.ApkConstant;
import com.fanwe.hybrid.event.EExitApp;
import com.fanwe.hybrid.event.EJsLogout;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.map.tencent.SDTencentMapManager;
import com.fanwe.hybrid.push.PushRunnable;
import com.fanwe.hybrid.umeng.UmengPushManager;
import com.fanwe.hybrid.utils.RetryInitWorker;
import com.fanwe.lib.cache.SDDisk;
import com.fanwe.lib.recorder.SDMediaRecorder;
import com.fanwe.library.SDLibrary;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.receiver.SDHeadsetPlugReceiver;
import com.fanwe.library.receiver.SDNetworkReceiver;
import com.fanwe.library.utils.LogUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.live.BuildConfig;
import com.fanwe.live.DebugHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveIniter;
import com.fanwe.live.R;
import com.fanwe.live.activity.LiveLoginActivity;
import com.fanwe.live.common.AppRuntimeWorker;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.common.JsonObjectConverter;
import com.fanwe.live.dao.UserModelDao;
import com.fanwe.live.event.EOnCallStateChanged;
import com.fanwe.live.event.EUserLoginSuccess;
import com.fanwe.live.event.EUserLogout;
import com.fanwe.live.model.App_userinfoActModel;
import com.fanwe.live.utils.StorageFileUtils;
import com.fanwei.jubaosdk.shell.FWPay;
import com.squareup.leakcanary.LeakCanary;
import com.sunday.eventbus.SDEventManager;
import com.tencent.bugly.imsdk.crashreport.CrashReport;
import com.tencent.rtmp.ITXLiveBaseListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import de.greenrobot.event.SubscriberExceptionEvent;

public class App extends Application implements ITXLiveBaseListener
{
    private static App instance;
    private PushRunnable pushRunnable;

    public static App getApplication()
    {
        return instance;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        init();
    }

    private void init()
    {
        StrictMode.VmPolicy.Builder builder =new StrictMode.VmPolicy.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        StrictMode.setVmPolicy(builder.build());
        if (SDPackageUtil.isMainProcess(this))
        {
            // ?????????
            SDLibrary.getInstance().init(this);

            SDDisk.init(this);
            SDDisk.setGlobalObjectConverter(new JsonObjectConverter());
            SDDisk.setDebug(ApkConstant.DEBUG);

            LeakCanary.install(this);
            MobclickAgent.setCatchUncaughtExceptions(false);
            UMShareAPI.get(this);
            SDEventManager.register(this);
            SDNetworkReceiver.registerReceiver(this);
            SDHeadsetPlugReceiver.registerReceiver(this);
            x.Ext.init(this);
            SDTencentMapManager.getInstance().init(this);
            new LiveIniter().init(this);
            initSystemListener();
            SDMediaRecorder.getInstance().init(this);
            LogUtil.isDebug = ApkConstant.DEBUG;
            DebugHelper.init(this);

            if (ApkConstant.DEBUG)
            {
                /*//??????sdk??????
                TXLiveBase.getInstance().setLogLevel(TXLiveConstants.LOG_LEVEL_DEBUG);
                TXLiveBase.getInstance().listener = this;*/
                LogUtil.i("Tencent Live SDK Version:" + TXLiveBase.getSDKVersionStr());
            }

            //??????????????????
            FWPay.init(this, getResources().getString(R.string.app_id), true);
        }
        // ??????????????????????????????????????????
        UmengPushManager.init(this);

        Context context = this;
        // ??????????????????
        String packageName = context.getPackageName();
        // ?????????????????????
        String processName = getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        strategy.setAppChannel(BuildConfig.FLAVOR);
        strategy.setAppVersion(BuildConfig.VERSION_CODE + "");
        strategy.setAppPackageName(packageName);
        CrashReport.setIsDevelopmentDevice(context, BuildConfig.DEBUG);
        CrashReport.initCrashReport(context, "54681bc255", ApkConstant.DEBUG,strategy);
    }
    /**
     * ?????????????????????????????????
     *
     * @param pid ?????????
     * @return ?????????
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initSystemListener()
    {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String incomingNumber)
            {
                EOnCallStateChanged event = new EOnCallStateChanged();
                event.state = state;
                event.incomingNumber = incomingNumber;
                SDEventManager.post(event);
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public boolean isPushStartActivity(Class<?> clazz)
    {
        boolean result = false;
        if (pushRunnable != null)
        {
            result = pushRunnable.getStartActivity() == clazz;
        }
        return result;
    }

    public void setPushRunnable(PushRunnable pushRunnable)
    {
        this.pushRunnable = pushRunnable;
    }

    public PushRunnable getPushRunnable()
    {
        return pushRunnable;
    }

    public void startPushRunnable()
    {
        if (pushRunnable != null)
        {
            pushRunnable.run();
            pushRunnable = null;
        }
    }


    public void exitApp(boolean isBackground)
    {
        AppRuntimeWorker.logout();
        SDActivityManager.getInstance().finishAllActivity();
        EExitApp event = new EExitApp();
        SDEventManager.post(event);
        if (!isBackground)
        {
            System.exit(0);
        }
    }

    /**
     * ????????????
     *
     * @param post
     */
    public void logout(boolean post)
    {
        logout(post, true, false);
    }

    public void logout(boolean post, boolean isStartLogin, boolean isStartH5Main)
    {
        UserModelDao.delete();
        AppRuntimeWorker.setUsersig(null);
        AppRuntimeWorker.logout();
        CommonInterface.requestLogout(null);
        RetryInitWorker.getInstance().start();
        StorageFileUtils.deleteCrop_imageFile();

        if (isStartLogin)
        {
            Intent intent = new Intent(this, LiveLoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SDActivityManager.getInstance().getLastActivity().startActivity(intent);
        } else if (isStartH5Main)
        {
            //????????????H5??????
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            SDActivityManager.getInstance().getLastActivity().startActivity(intent);
        }

        if (post)
        {
            EUserLogout event = new EUserLogout();
            SDEventManager.post(event);
        }
    }

    /**
     * ????????????
     *
     * @param event
     */
    public void onEventMainThread(EJsLogout event)
    {
        logout(true);
    }

    public void onEventMainThread(EUserLoginSuccess event)
    {
        AppRuntimeWorker.setUsersig(null);
        CommonInterface.requestMyUserInfo(new AppRequestCallback<App_userinfoActModel>()
        {
            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.getStatus() == 1)
                {
                    CommonInterface.requestUsersig(null);
                }
            }
        });
    }

    public void onEventMainThread(SubscriberExceptionEvent event)
    {
        LogUtil.e("onEventMainThread:" + event.throwable.toString());
    }

    @Override
    public void onTerminate()
    {
        SDEventManager.unregister(instance);
        SDNetworkReceiver.unregisterReceiver(this);
        SDHandlerManager.stopBackgroundHandler();
        SDMediaRecorder.getInstance().release();
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void OnLog(int level, String module, String log)
    {
        switch (level)
        {
            case TXLiveConstants.LOG_LEVEL_ERROR:
                Log.e(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_WARN:
                Log.w(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_INFO:
                Log.i(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            case TXLiveConstants.LOG_LEVEL_DEBUG:
                Log.d(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
                break;
            default:
                Log.d(LiveConstant.LiveSdkTag.TAG_SDK_TENCENT, module + "----------" + log);
        }
    }
}
