package com.off.appview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.lib.pulltorefresh.SDPullToRefreshView;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.view.pulltorefresh.IPullToRefreshViewWrapper;
import com.fanwei.jubaosdk.shell.OnPayResultListener;
import com.google.gson.Gson;
import com.off.CarMallAdapter;
import com.off.bean.CarsBean;
import com.off.bean.CarsModel;
import com.off.util.CarsUtils;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jetbrains.annotations.NotNull;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/**
 * 座驾商城
 */
public class LiveMallCarsTabMallView extends BaseAppView {
    public LiveMallCarsTabMallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LiveMallCarsTabMallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LiveMallCarsTabMallView(Context context) {
        super(context);
        init();
    }

    @ViewInject(R.id.rvCars)
    private RecyclerView rvCars;

    @ViewInject(R.id.svgaAnim)
    private SVGAImageView svgaAnim;

    private List<CarsBean> mListModel = new ArrayList<>();
    private CarMallAdapter mAdapter;

    private void init() {
        setContentView(R.layout.frag_cars_mall);
        getPullToRefreshViewWrapper().setPullToRefreshView((SDPullToRefreshView) findViewById(R.id.view_pull_to_refresh));


        mAdapter = new CarMallAdapter(mListModel, new CarMallAdapter.ClickInterface() {
            @Override
            public void play(int position) {
//                svgaAnim.startAnimation();
                loadAnimation(mListModel.get(position).getCar_type());
            }

            @Override
            public void buy(int position) {
                requestPay(position);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCars.setLayoutManager(layoutManager);
        rvCars.setAdapter(mAdapter);
        getCars();
        initPullToRefresh();
    }


    private void initPullToRefresh() {
        getPullToRefreshViewWrapper().setModePullFromHeader();
        getPullToRefreshViewWrapper().setOnRefreshCallbackWrapper(new IPullToRefreshViewWrapper.OnRefreshCallbackWrapper() {
            @Override
            public void onRefreshingFromHeader() {
                getCars();
            }

            @Override
            public void onRefreshingFromFooter() {

            }
        });
    }

    private void getCars() {
        CommonInterface.requestCars(new AppRequestCallback<CarsModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                CarsModel result = new Gson().fromJson(sdResponse.getDecryptedResult(), CarsModel.class);
                mListModel.clear();
                mListModel.addAll(result.getList());
                mAdapter.notifyDataSetChanged();
                getPullToRefreshViewWrapper().stopRefreshing();
            }


            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }

    private void loadAnimation(String type) {
        String animPath = CarsUtils.getCarPath(type);
        SVGAParser parser = new SVGAParser(getContext());
        resetDownloader(parser);
        try {

            //"mini_zxg.svga"
            //new URL("https://github.com/yyued/SVGA-Samples/blob/master/kingset.svga?raw=true"),
            if(CarsUtils.isFileExistsAsAsset(getContext().getAssets(),animPath)){
                parser.parse(animPath, new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItemWithSpannableText());
                        svgaAnim.setImageDrawable(drawable);
                        svgaAnim.startAnimation();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
            else{
                parser.parse(new URL(type), new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem, requestDynamicItemWithSpannableText());
                        svgaAnim.setImageDrawable(drawable);
                        svgaAnim.startAnimation();
                    }

                    @Override
                    public void onError() {

                    }
                });
            }
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     *
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }

    /**
     * 你可以设置富文本到 ImageKey 相关的元素上
     * 富文本是会自动换行的，不要设置过长的文本
     *
     * @return
     */
    private SVGADynamicEntity requestDynamicItemWithSpannableText() {
        SVGADynamicEntity dynamicEntity = new SVGADynamicEntity();
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Pony 送了一打风油精给主播");
        spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.YELLOW), 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(28);
        dynamicEntity.setDynamicText(new StaticLayout(
                spannableStringBuilder,
                0,
                spannableStringBuilder.length(),
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
        ), "banner");
        dynamicEntity.setDynamicDrawer(new Function2<Canvas, Integer, Boolean>() {
            @Override
            public Boolean invoke(Canvas canvas, Integer frameIndex) {
                Paint aPaint = new Paint();
                aPaint.setColor(Color.WHITE);
                canvas.drawCircle(50, 54, frameIndex % 5, aPaint);
                return false;
            }
        }, "banner");
        return dynamicEntity;
    }

    private void requestPay(int position) {
        CommonInterface.requestPayCar(mListModel.get(position).getId()+"", new AppRequestCallback<App_payActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("正在启动插件");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    SDToast.showToast("购买完成");
                }
            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner() {
        @Override
        public void onSuccess() {
//            et_money.setText("");
        }

        @Override
        public void onDealing() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onNetWork() {

        }

        @Override
        public void onOther() {

        }
    };

    private OnPayResultListener jbfPayResultListener = new OnPayResultListener() {

        @Override
        public void onSuccess(Integer integer, String s, String s1) {
//            et_money.setText("");
        }

        @Override
        public void onFailed(Integer integer, String s, String s1) {
        }
    };

}
