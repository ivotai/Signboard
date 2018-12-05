package com.unicorn.signboard.detail.merchant

import com.unicorn.signboard.R
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.merchant.add.Merchant

class MerchantDetailAct:BaseAct(){

    override val layoutId = R.layout.detail_merchant

    private lateinit var merchant:Merchant

    override fun initViews() {
        merchant = intent.getSerializableExtra(Key.merchant) as Merchant
    }

    override fun bindIntent() {
    }

}


