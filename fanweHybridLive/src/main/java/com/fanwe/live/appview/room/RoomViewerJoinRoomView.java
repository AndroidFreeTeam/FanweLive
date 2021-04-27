package com.fanwe.live.appview.room;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;

import com.fanwe.live.IMHelper;
import com.fanwe.live.LiveConstant;
import com.fanwe.live.LiveInformation;
import com.fanwe.live.R;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsg;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;
import com.fanwe.live.model.custommsg.CustomMsgViewerJoin;
import com.fanwe.live.model.custommsg.CustomMsgViewerQuit;
import com.fanwe.live.model.custommsg.MsgModel;
import com.fanwe.live.view.LiveViewerJoinRoomView;
import com.off.util.CarsUtils;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGADynamicEntity;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class RoomViewerJoinRoomView extends RoomLooperMainView<CustomMsgViewerJoin>
{
    public RoomViewerJoinRoomView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public RoomViewerJoinRoomView(Context context)
    {
        super(context);
    }

    private static final long DURATION_LOOPER = 1000;

    private LiveViewerJoinRoomView view_viewer_join;
    private SVGAImageView svgaAnim;

    @Override
    protected int onCreateContentView()
    {
        return R.layout.view_room_viewer_join_room;
    }

    @Override
    protected void onBaseInit()
    {
        super.onBaseInit();

        view_viewer_join = find(R.id.view_viewer_join);

        svgaAnim = find(R.id.svgaAnim);
    }

    @Override
    public void onMsgViewerJoin(CustomMsgViewerJoin msg)
    {
        super.onMsgViewerJoin(msg);

        if (msg.getSender().isProUser() || msg.getSender().getIs_has_car() == 1)
        {
            if (getQueue().contains(msg))
            {
                // 不处理
            } else
            {
                if (msg.equals(view_viewer_join.getMsg()) && view_viewer_join.isPlaying())
                {
                    // 不处理
                } else
                {
                    offerModel(msg);
                }
            }
        }
    }

    @Override
    public void onMsgViewerQuit(CustomMsgViewerQuit msg)
    {
        super.onMsgViewerQuit(msg);

        if (msg.getSender().isProUser())
        {
            Iterator<CustomMsgViewerJoin> it = getQueue().iterator();
            while (it.hasNext())
            {
                CustomMsgViewerJoin item = it.next();
                if (msg.getSender().equals(item.getSender()))
                {
                    it.remove();
                }
            }
        }
    }

    @Override
    protected long getLooperPeriod()
    {
        return DURATION_LOOPER;
    }

    @Override
    protected void onLooperWork(LinkedList<CustomMsgViewerJoin> queue)
    {
        if (view_viewer_join.canPlay())
        {
            CustomMsgViewerJoin newMsg = queue.poll();
            if(null == newMsg){
                return;
            }
            view_viewer_join.playMsg(newMsg);

            //是否拥有座驾
            if (newMsg.getSender().getIs_has_car() == 1) {
                loadCarNotify(newMsg.getSender());
                loadAnimation(newMsg.getSender().getCar_type());
            }
        }
    }
    private void loadCarNotify(UserModel sender){
        CustomMsgLargeGift customMsgLargeGift = new CustomMsgLargeGift();
        customMsgLargeGift.setRoom_id(getLiveActivity().getRoomId());
        customMsgLargeGift.setSender(sender);
        customMsgLargeGift.setDesc("CAR");
        customMsgLargeGift.setType(LiveConstant.CustomMsgType.MSG_LARGE_GIFT);
        IMHelper.postMsgLocal(customMsgLargeGift,getLiveActivity().getGroupId());
    }

    private void loadAnimation(String type) {
        String animPath = CarsUtils.getCarPath(type);
        SVGAParser parser = new SVGAParser(getContext());
        resetDownloader(parser);
        try {
            if(CarsUtils.isFileExistsAsAsset(getContext().getAssets(),animPath)){
                parser.parse(animPath, new SVGAParser.ParseCompletion() {
                    @Override
                    public void onComplete(SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
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
                    public void onComplete(SVGAVideoEntity videoItem) {
                        SVGADrawable drawable = new SVGADrawable(videoItem);
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
}
