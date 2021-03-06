package com.fanwe.auction.appview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.auction.model.App_pai_user_goods_detailActModel;
import com.fanwe.auction.model.PaiUserGoodsDetailDataInfoModel;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.live.R;
import com.fanwe.live.appview.BaseAppView;

/**
 * Created by Administrator on 2016/8/10.
 */
public class AuctionGoodsDetailStatusOtherView extends BaseAppView
{
    private LinearLayout ll_goods_detail_status_ohter_bg;
    private ImageView iv_other_status;
    private TextView tv_other_status_desc1;
    private TextView tv_other_status_desc2;

    public AuctionGoodsDetailStatusOtherView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    public AuctionGoodsDetailStatusOtherView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public AuctionGoodsDetailStatusOtherView(Context context)
    {
        super(context);
        init();
    }

    protected void init()
    {
        setContentView(R.layout.view_auction_goods_detail_status_1);
        register();
    }

    private void register()
    {
        iv_other_status = find(R.id.iv_other_status);
        tv_other_status_desc1 = find(R.id.tv_other_status_desc1);
        tv_other_status_desc2 = find(R.id.tv_other_status_desc2);
        ll_goods_detail_status_ohter_bg = find(R.id.ll_goods_detail_status_ohter_bg);
    }

    public void bindData(App_pai_user_goods_detailActModel actModel)
    {
        if (actModel != null && actModel.getData() != null && actModel.getData().getInfo() != null)
        {
            PaiUserGoodsDetailDataInfoModel info = actModel.getData().getInfo();
            if (info.getStatus() == 1)
            {
                iv_other_status.setImageResource(R.drawable.ic_auction_detail_success);
                tv_other_status_desc1.setText("????????????");
                tv_other_status_desc2.setText("?????????");
                tv_other_status_desc2.setTextColor(SDResourcesUtil.getColor(R.color.auction_main_color));
                tv_other_status_desc2.setBackgroundResource(R.drawable.auction_layer_auction_goods_detail_status_yellow);
                ll_goods_detail_status_ohter_bg.setBackgroundColor(SDResourcesUtil.getColor(R.color.auction_main_color));
            } else if (info.getStatus() == 2)
            {
                iv_other_status.setImageResource(R.drawable.ic_auction_detail_fail);
                tv_other_status_desc1.setText("??????");
                tv_other_status_desc2.setText("????????????");
            } else if (info.getStatus() == 3)
            {
                iv_other_status.setImageResource(R.drawable.ic_auction_detail_fail);
                tv_other_status_desc1.setText("????????????");
                tv_other_status_desc2.setText("??????????????????");
            }
            if (info.getStatus() == 4)
            {
                iv_other_status.setImageResource(R.drawable.ic_auction_detail_success);
                tv_other_status_desc1.setText("????????????");
                tv_other_status_desc2.setText("????????????");
                tv_other_status_desc2.setTextColor(SDResourcesUtil.getColor(R.color.auction_main_color));
                tv_other_status_desc2.setBackgroundResource(R.drawable.auction_layer_auction_goods_detail_status_yellow);
                ll_goods_detail_status_ohter_bg.setBackgroundColor(SDResourcesUtil.getColor(R.color.auction_main_color));
            }


        }
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
    }
}
