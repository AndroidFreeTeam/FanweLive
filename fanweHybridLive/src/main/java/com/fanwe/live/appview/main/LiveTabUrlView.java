package com.fanwe.live.appview.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.GridView;

import com.fanwe.hybrid.http.AppHttpUtil;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.common.SDHandlerManager;
import com.fanwe.library.model.SDTaskRunnable;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.webview.CustomWebView;
import com.fanwe.library.webview.DefaultWebViewClient;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveTabCategoryAdapter;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.HomeTabTitleModel;
import com.fanwe.live.model.Index_new_videoActModel;
import com.fanwe.live.model.LiveRoomModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwe.o2o.jshandler.O2OShoppingLiveJsHander;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.fanwe.live.appview.H5AppViewWeb.no_network_url;

/**
 * 首页直播分类URLview
 */
public class LiveTabUrlView extends LiveTabBaseView
{
    public LiveTabUrlView(Context context)
    {
        super(context);
        init();
    }

    public LiveTabUrlView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveTabUrlView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private CustomWebView customWebView;

    private HomeTabTitleModel mTabTitleModel;
    private String mUrl;

    /**
     * 设置直播分类实体
     *
     * @param tabTitleModel
     */
    public void setTabTitleModel(HomeTabTitleModel tabTitleModel)
    {
        mTabTitleModel = tabTitleModel;
        if(null != mTabTitleModel){
            mUrl = mTabTitleModel.getUrl();
            Log.e("URL",mUrl);
        }
        startLoopRunnable();
    }

    private void init()
    {
        setContentView(R.layout.view_live_tab_url);
        customWebView = (CustomWebView) findViewById(R.id.cus_webview);
        initWebView();
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
        AppHttpUtil.getInstance().synchronizeHttpCookieToWebView(mUrl);
        customWebView.get(mUrl);
    }

    @Override
    public void scrollToTop()
    {
    }

    @Override
    protected void onRoomClosed(final int roomId)
    {

    }


    private void initWebView()
    {
        customWebView.addJavascriptInterface(new O2OShoppingLiveJsHander(getActivity(), customWebView));
        customWebView.getSettings().setBuiltInZoomControls(true);

        customWebView.setWebViewClient(new DefaultWebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {

                if (url.contains("vipshop://")||url.contains("tbopen://"))
                {
                    return false;
                }else if(!url.toLowerCase().startsWith("http") && !url.toLowerCase().startsWith("https")){
                    try{
                        getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }catch (Exception ex){
                        return super.shouldOverrideUrlLoading(view, url);
                    }
                }
                else
                {
                    return super.shouldOverrideUrlLoading(view, url);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                super.onReceivedError(view, errorCode, description, failingUrl);
                customWebView.loadUrl(no_network_url);
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
            }
        });

        customWebView.setWebChromeClientListener(new WebChromeClient()
        {

            @Override
            public void onReceivedTitle(WebView view, String title)
            {
            }
        });
    }

}
