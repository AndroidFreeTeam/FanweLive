package com.fanwe.live.span;

import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fanwe.library.common.SDBitmapCache;
import com.fanwe.lib.span.SDDynamicDrawableSpan;
import com.fanwe.live.R;
import com.fanwe.live.utils.GlideUtil;

public class SDNetImageSpan extends SDDynamicDrawableSpan
{

    private String url;

    public SDNetImageSpan(View view)
    {
        super(view);
    }

    public SDNetImageSpan setImage(String url)
    {
        this.url = url;
        return this;
    }

    @Override
    protected int getDefaultDrawableResId()
    {
        return R.drawable.nopic_expression;
    }

    @Override
    protected Bitmap onGetBitmap()
    {
        Bitmap bitmap = SDBitmapCache.getInstance().get(url);

        if (bitmap == null)
        {
            GlideUtil.load(url).asBitmap().into(new SimpleTarget<Bitmap>()
            {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation)
                {
                    SDBitmapCache.getInstance().put(url, resource);
                    View view = getView();
                    if(null != view){
                        view.postInvalidate();
                    }
                }
            });
        }
        return bitmap;
    }

}
