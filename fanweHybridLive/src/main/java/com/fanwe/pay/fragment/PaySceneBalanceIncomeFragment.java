package com.fanwe.pay.fragment;

import com.fanwe.live.common.AppRuntimeWorker;

/**
 * Created by yhz on 2017/1/9.按场付费收费记录
 */

public class PaySceneBalanceIncomeFragment extends PayBalanceBaseFragment
{
    @Override
    protected void init()
    {
        this.type = 1;
        this.live_pay_type=1;
        super.init();
        adapter.setText("贡献" + AppRuntimeWorker.getDiamondName() + ":");
    }
}
