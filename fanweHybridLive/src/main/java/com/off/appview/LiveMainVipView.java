package com.off.appview;

import android.app.Activity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.fanwe.hybrid.common.CommonOpenSDK;
import com.fanwe.hybrid.http.AppRequestCallback;
import com.fanwe.hybrid.listner.PayResultListner;
import com.fanwe.hybrid.model.PaySdkModel;
import com.fanwe.library.adapter.http.model.SDResponse;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.listener.SDItemClickCallback;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.live.R;
import com.fanwe.live.adapter.LiveRechargePaymentAdapter;
import com.fanwe.live.adapter.LiveRechrgeVipPaymentRuleAdapter;
import com.fanwe.live.appview.BaseAppView;
import com.fanwe.live.common.CommonInterface;
import com.fanwe.live.model.App_UserVipPurchaseActModel;
import com.fanwe.live.model.App_payActModel;
import com.fanwe.live.model.PayItemModel;
import com.fanwe.live.model.PayModel;
import com.fanwe.live.model.RuleItemModel;
import com.fanwei.jubaosdk.shell.OnPayResultListener;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yan on 2018/12/30 0030
 */
public class LiveMainVipView extends BaseAppView {
    private Activity mContext;

    public LiveMainVipView(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        init();
    }

    public LiveMainVipView(Activity context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public LiveMainVipView(Activity context) {
        super(context);
        this.mContext = context;
        init();
    }

    @ViewInject(R.id.tv_user_vip_deadline)
    private TextView tv_user_vip_deadline;

    @ViewInject(R.id.ll_payment)
    private SDGridLinearLayout ll_payment;

    @ViewInject(R.id.ll_payment_rule)
    private SDGridLinearLayout ll_payment_rule;

    private LiveRechargePaymentAdapter adapterPayment;
    private List<PayItemModel> listPayment = new ArrayList<>();

    private LiveRechrgeVipPaymentRuleAdapter adapterPaymentRule;
    private List<RuleItemModel> listPaymentRule = new ArrayList<>();


    private int pay_id;
    private int rule_id;


    private void init() {
        setContentView(R.layout.act_live_recharge_vip);

//        mTitle.setMiddleTextTop("VIP??????");

        //????????????
        adapterPayment = new LiveRechargePaymentAdapter(listPayment, mContext);
        adapterPayment.setItemClickCallback(new SDItemClickCallback<PayItemModel>() {
            @Override
            public void onItemClick(int position, PayItemModel item, View view) {
                adapterPayment.getSelectManager().performClick(item);
            }
        });
        ll_payment.setAdapter(adapterPayment);

        //????????????
        adapterPaymentRule = new LiveRechrgeVipPaymentRuleAdapter(listPaymentRule, mContext);
        adapterPaymentRule.setItemClickCallback(new SDItemClickCallback<RuleItemModel>() {
            @Override
            public void onItemClick(int position, RuleItemModel item, View view) {
                rule_id = item.getId();
                clickPaymentRule(item);
            }
        });
        ll_payment_rule.setAdapter(adapterPaymentRule);

        requestVipData();
    }

    /**
     * ??????????????????
     *
     * @param model
     */
    private void clickPaymentRule(RuleItemModel model) {
        if (!validatePayment()) {
            return;
        }
        requestPay();
    }

    private void requestPay() {
        CommonInterface.requestPayVip(pay_id, rule_id, new AppRequestCallback<App_payActModel>() {
            @Override
            protected void onStart() {
                super.onStart();
                showProgressDialog("??????????????????");
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
                dismissProgressDialog();
            }

            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {
                    PayModel payModel = actModel.getPay();
                    if (payModel != null) {
                        PaySdkModel paySdkModel = payModel.getSdk_code();
                        if (paySdkModel != null) {
                            CommonOpenSDK.dealPayRequestSuccess(actModel, getActivity(), payResultListner, jbfPayResultListener);
                        }
                    }
                }
            }
        });
    }

    private PayResultListner payResultListner = new PayResultListner() {
        @Override
        public void onSuccess() {

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

        }

        @Override
        public void onFailed(Integer integer, String s, String s1) {
        }
    };

    private boolean validatePayment() {
        PayItemModel payment = adapterPayment.getSelectManager().getSelectedItem();
        if (payment == null) {
            SDToast.showToast("?????????????????????");
            return false;
        }
        pay_id = payment.getId();

        return true;
    }

    private void requestVipData() {
        CommonInterface.requestVipPurchase(new AppRequestCallback<App_UserVipPurchaseActModel>() {
            @Override
            protected void onSuccess(SDResponse resp) {
                if (actModel.isOk()) {

                    if (actModel.getIs_vip() == 0) {
                        tv_user_vip_deadline.setTextColor(SDResourcesUtil.getColor(R.color.res_text_gray_m));
                    } else {
                        tv_user_vip_deadline.setTextColor(SDResourcesUtil.getColor(R.color.res_main_color));
                    }
                    SDViewBinder.setTextView(tv_user_vip_deadline, actModel.getVip_expire_time());
                    adapterPayment.updateData(actModel.getPay_list());
                    adapterPaymentRule.updateData(actModel.getRule_list());

                    int defaultPayIndex = -1;
                    List<PayItemModel> listPay = actModel.getPay_list();
                    if (listPay != null) {
                        int i = 0;
                        for (PayItemModel pay : listPay) {
                            if (pay_id == pay.getId()) {
                                defaultPayIndex = i;
                                break;
                            }
                            i++;
                        }
                        if (defaultPayIndex < 0) {
                            defaultPayIndex = 0;
                            pay_id = 0;
                        }
                    }
                    adapterPayment.getSelectManager().setSelected(defaultPayIndex, true);
                }
            }

            @Override
            protected void onFinish(SDResponse resp) {
                super.onFinish(resp);
            }
        });
    }
}
