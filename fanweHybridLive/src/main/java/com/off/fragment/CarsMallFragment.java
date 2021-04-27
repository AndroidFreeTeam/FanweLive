package com.off.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.hybrid.fragment.BaseFragment;
import com.off.appview.LiveMallCarsTabMallView;

/**
 * 座驾商城
 */
public class CarsMallFragment extends BaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return new LiveMallCarsTabMallView(container.getContext());
    }

/*

 @Override
    protected int onCreateContentView() {
        return R.layout.frag_cars_mall;
    }

 @ViewInject(R.id.rvCars)
    private RecyclerView rvCars;

    @ViewInject(R.id.svgaAnim)
    private SVGAImageView svgaAnim;


    private List<CarsBean> mListModel = new ArrayList<>();
    private CarMallAdapter mAdapter;


    @Override
    protected void init() {
        super.init();

        mAdapter = new CarMallAdapter(mListModel, new CarMallAdapter.ClickInterface() {
            @Override
            public void play(int position) {
//                svgaAnim.startAnimation();
                loadAnimation(mListModel.get(position).getIcon_gif());
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

    }

    private void getCars() {
        CommonInterface.requestCars(new AppRequestCallback<CarsModel>() {
            @Override
            protected void onSuccess(SDResponse sdResponse) {
                Gson gson = new Gson();
                CarsModel result = gson.fromJson(sdResponse.getResult(), CarsModel.class);
                mListModel.addAll(result.getList());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }

    private void loadAnimation(String path){
        SVGAParser parser = new SVGAParser(getContext());
        resetDownloader(parser);
        try {//new URL("https://github.com/yyued/SVGA-Samples/blob/master/kingset.svga?raw=true"),
            parser.parse( "mini_zxg.svga",new SVGAParser.ParseCompletion() {
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
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    *//**
     * 设置下载器，这是一个可选的配置项。
     * @param parser
     *//*
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

    *//**
     * 你可以设置富文本到 ImageKey 相关的元素上
     * 富文本是会自动换行的，不要设置过长的文本
     * @return
     *//*
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
        CommonInterface.requestPayCar(mListModel.get(position).getId(), new AppRequestCallback<App_payActModel>()
        {
            @Override
            protected void onStart()
            {
                super.onStart();
                showProgressDialog("正在启动插件");
            }

            @Override
            protected void onFinish(SDResponse resp)
            {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse resp)
            {
                if (actModel.isOk())
                {
                    CommonOpenSDK.dealPayRequestSuccess(actModel, getActivity(), payResultListner, jbfPayResultListener);
                }
            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner()
    {
        @Override
        public void onSuccess()
        {
//            et_money.setText("");
        }

        @Override
        public void onDealing()
        {

        }

        @Override
        public void onFail()
        {

        }

        @Override
        public void onCancel()
        {

        }

        @Override
        public void onNetWork()
        {

        }

        @Override
        public void onOther()
        {

        }
    };

    private OnPayResultListener jbfPayResultListener = new OnPayResultListener()
    {

        @Override
        public void onSuccess(Integer integer, String s, String s1)
        {
//            et_money.setText("");
        }

        @Override
        public void onFailed(Integer integer, String s, String s1)
        {
        }
    };*/
}
