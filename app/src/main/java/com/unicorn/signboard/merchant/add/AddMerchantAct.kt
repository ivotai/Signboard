package com.unicorn.signboard.merchant.add

import com.unicorn.signboard.R
import com.unicorn.signboard.app.base.BaseAct
import kotlinx.android.synthetic.main.act_add_merchant.*


class AddMerchantAct : BaseAct() {

    override val layoutId = R.layout.act_add_merchant

    override fun initViews() {
        titleBar.setTitle("商户录入")
    }

    val merchant = Merchant()

    override fun bindIntent() {
        // TODO 百度地图定位
    }

}