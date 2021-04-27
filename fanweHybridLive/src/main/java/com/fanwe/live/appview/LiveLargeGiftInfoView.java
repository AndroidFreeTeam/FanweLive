package com.fanwe.live.appview;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.lib.animator.SDAnim;
import com.fanwe.lib.animator.listener.SDAnimatorListener;
import com.fanwe.library.model.SDDelayRunnable;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.live.R;
import com.fanwe.live.model.UserModel;
import com.fanwe.live.model.custommsg.CustomMsgLargeGift;

/**
 * 大型礼物通知view
 */
public class LiveLargeGiftInfoView extends BaseAppView
{
    public LiveLargeGiftInfoView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public LiveLargeGiftInfoView(Context context)
    {
        super(context);
        init();
    }

    private static final long DURATION_ANIM = 12 * 1000;
    private static final long DURATION_CAR_ANIM = 4 * 1000;
    /**
     * view在屏幕外的偏移量，为了让平移看上去更顺滑
     */
    private static final int DISTANCE_OFFSET = SDViewUtil.dp2px(30);

    private RelativeLayout rl_root_view;
    private TextView tv_title;
    private TextView tv_content;
    private CustomMsgLargeGift mMsg;
    private SDAnim mAnim;
    private SDAnim mCarAnim;

    private void init()
    {
        setContentView(R.layout.view_live_large_gift_info);
        rl_root_view = (RelativeLayout) findViewById(R.id.rl_root_view);
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        SDViewUtil.setInvisible(this);
    }

    public CustomMsgLargeGift getMsg()
    {
        return mMsg;
    }

    public boolean isPlaying()
    {
        if (mAnim != null && mCarAnim != null)
        {
            return mAnim.isRunning() || mCarAnim.isRunning();
        } else
        {
            return false;
        }
    }

    public void playMsg(CustomMsgLargeGift msg)
    {
        Log.e("CustomMsgLargeGift",msg.getDesc());
        if (isPlaying())
        {
            Log.e("isPlaying","isPlaying");
            return;
        }
        mMsg = msg;
        if("CAR".equals(msg.getDesc())){
            rl_root_view.setBackgroundResource(R.drawable.bg_car_info);
            SDViewUtil.setVisible(tv_title);
            tv_title.setText(msg.getSender().getNick_name());
            tv_content.setText("乘坐【"+msg.getSender().getCar_name()+"】驾到");
            mPlayCarAnimRunnable.runDelay(500);
        }else{
            rl_root_view.setBackgroundResource(R.drawable.bg_large_gift_info);
            SDViewUtil.setGone(tv_title);
            tv_title.setText("");
            tv_content.setText(msg.getDesc());
            mPlayAnimRunnable.runDelay(500);
        }
    }

    private SDDelayRunnable mPlayAnimRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            playAnim();
        }
    };

    private void playAnim()
    {
        if (mAnim == null)
        {
            mAnim = SDAnim.from(this)
                    .setDuration(DURATION_ANIM)
                    .setInterpolator(new LinearInterpolator())
                    .addListener(new SDAnimatorListener()
                    {
                        @Override
                        public void onAnimationStart(Animator animation)
                        {
                            super.onAnimationStart(animation);
                            SDViewUtil.setVisible(LiveLargeGiftInfoView.this);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);
                            SDViewUtil.resetView(LiveLargeGiftInfoView.this);
                            SDViewUtil.setInvisible(LiveLargeGiftInfoView.this);
                            mMsg = null;
                        }
                    });
        }

        int x1 = SDViewUtil.getScreenWidth() + DISTANCE_OFFSET;
        int x2 = -SDViewUtil.getWidth(this) - DISTANCE_OFFSET;
        mAnim = mAnim.moveToX(x1, x2);
        mAnim.start();
    }

    private void stopAnim()
    {
        if (mAnim != null)
        {
            mAnim.cancel();
        }
    }

    private SDDelayRunnable mPlayCarAnimRunnable = new SDDelayRunnable()
    {
        @Override
        public void run()
        {
            playCarAnim();
        }
    };

    private void playCarAnim()
    {
        if (mCarAnim == null)
        {
            mCarAnim = SDAnim.from(this)
                    .setDuration(DURATION_CAR_ANIM)
                    .setInterpolator(new LinearInterpolator())
                    .addListener(new SDAnimatorListener()
                    {
                        @Override
                        public void onAnimationStart(Animator animation)
                        {
                            super.onAnimationStart(animation);
                            SDViewUtil.setVisible(LiveLargeGiftInfoView.this);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation)
                        {
                            super.onAnimationEnd(animation);
                            SDViewUtil.resetView(LiveLargeGiftInfoView.this);
                            SDViewUtil.setInvisible(LiveLargeGiftInfoView.this);
                            mMsg = null;
                        }
                    });
        }
        int x1 = SDViewUtil.getScreenWidth() + DISTANCE_OFFSET;
        int x2 = -SDViewUtil.getWidth(this) - DISTANCE_OFFSET;
        mCarAnim = mCarAnim.moveToX(x1, x2);

       /* float x = (SDViewUtil.getScreenWidth()-SDViewUtil.getWidth(this))/2f;
        mCarAnim = mCarAnim.alpha(0f,1f,1f,0f).setAlignType(SDAnim.AlignType.Center).moveToX(x,x);*/
        mCarAnim.start();
    }

    private void stopCarAnim()
    {
        if (mCarAnim != null)
        {
            mCarAnim.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        mPlayAnimRunnable.removeDelay();
        stopAnim();
        mPlayCarAnimRunnable.removeDelay();
        stopCarAnim();
    }
}
