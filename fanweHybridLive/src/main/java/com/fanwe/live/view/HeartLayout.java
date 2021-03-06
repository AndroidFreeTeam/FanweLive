package com.fanwe.live.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.fanwe.library.utils.SDRandom;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeartLayout extends RelativeLayout
{

    private static final int HEART_WIDTH = SDViewUtil.dp2px(25);

    private List<Interpolator> listInterpolator = new ArrayList<Interpolator>();
    private List<String> listDrawableName = new ArrayList<String>();
    private Map<String, Drawable> mapNameDrawable = new HashMap<String, Drawable>();

    private int height;
    private int width;
    private LayoutParams heartLayoutParams;
    private Random random = new SDRandom();
    private int drawableHeight;
    private int drawableWidth;

    public HeartLayout(Context context)
    {
        super(context);
        init();
    }

    public HeartLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getHeartCount()
    {
        return getChildCount();
    }

    private void init()
    {

        listDrawableName.clear();
        listDrawableName.add("heart0");
        listDrawableName.add("heart1");
        listDrawableName.add("heart2");
        listDrawableName.add("heart3");
        listDrawableName.add("heart4");
        listDrawableName.add("heart5");
        listDrawableName.add("heart6");

        mapNameDrawable.clear();
        Drawable drawable = null;
        for (String item : listDrawableName)
        {
            int id = SDResourcesUtil.getIdentifierDrawable(item);
            if (id != 0)
            {
                drawable = getResources().getDrawable(id);
                if (drawable != null)
                {
                    mapNameDrawable.put(item, drawable);
                }
            }
        }

        drawableHeight = drawable.getIntrinsicHeight();
        drawableWidth = drawable.getIntrinsicWidth();

        createHeartLayoutParams();

        listInterpolator.add(new LinearInterpolator());
        listInterpolator.add(new AccelerateInterpolator());
        listInterpolator.add(new DecelerateInterpolator());
        listInterpolator.add(new AccelerateDecelerateInterpolator());

    }

    private void createHeartLayoutParams()
    {
        heartLayoutParams = new LayoutParams(HEART_WIDTH, HEART_WIDTH);
        heartLayoutParams.addRule(CENTER_HORIZONTAL, TRUE);// ?????????TRUE ????????? ??????true
        heartLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public String randomImageName()
    {
        int index = random.nextInt(listDrawableName.size());
        String name = listDrawableName.get(index);
        return name;
    }

    public void addHeart(String imageName)
    {
        Drawable drawable = mapNameDrawable.get(imageName);

        if (drawable != null)
        {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(drawable);
            imageView.setLayoutParams(heartLayoutParams);

            addView(imageView);

            Animator set = getAnimator(imageView);
            set.addListener(new AnimEndListener(imageView));
            set.start();
        }
    }

    private Animator getAnimator(View target)
    {
        AnimatorSet set = getEnterAnimtor(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set, bezierValueAnimator);
        finalSet.setInterpolator(listInterpolator.get(random.nextInt(listInterpolator.size())));
        finalSet.setTarget(target);
        return finalSet;
    }

    private AnimatorSet getEnterAnimtor(final View target)
    {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1f);
        AnimatorSet enter = new AnimatorSet();
        enter.setDuration(500);
        enter.setInterpolator(new LinearInterpolator());
        enter.playTogether(alpha, scaleX, scaleY);
        enter.setTarget(target);
        return enter;
    }

    private ValueAnimator getBezierValueAnimator(View target)
    {

        // ?????????????????????????????????- - ??????
        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));

        // ????????????????????? ???????????? ??????????????? ??? ??????
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, new PointF((width - drawableWidth) / 2, height - drawableHeight), new PointF(
                random.nextInt(getWidth()), 0));
        animator.addUpdateListener(new BezierListenr(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    /**
     * ????????????????????? ???
     *
     * @param scale
     */
    private PointF getPointF(int scale)
    {
        PointF pointF = new PointF();
        try
        {
            pointF.x = random.nextInt((width - 100));// ??????100 ??????????????? x???????????????,????????? ??????~~
            // ???Y?????? ???????????????????????? ?????????????????????,??????Y????????????????????? ??????????????????????????? ????????????????????????
            pointF.y = random.nextInt((height - 100)) / scale;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return pointF;
    }

    private class BezierListenr implements ValueAnimator.AnimatorUpdateListener
    {

        private View target;

        public BezierListenr(View target)
        {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation)
        {
            // ????????????????????????????????????????????????x y??? ?????????view ???????????????????????????????????????
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // ?????????????????????alpha??????
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter
    {
        private View target;

        public AnimEndListener(View target)
        {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation)
        {
            super.onAnimationEnd(animation);
            // ???????????????add ?????????view??????????????????,?????????view???????????????remove???
            removeView((target));
        }
    }
}
