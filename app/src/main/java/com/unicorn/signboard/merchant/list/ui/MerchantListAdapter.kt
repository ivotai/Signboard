package com.unicorn.signboard.merchant.list.ui

import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.merchant.add.Merchant
import kotlinx.android.synthetic.main.item_right_arrow.*

class MerchantListAdapter : MyAdapter<Merchant, MyHolder>(R.layout.item_right_arrow) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

    override fun convert(helper: MyHolder, item: Merchant) {
        helper.apply {
            tvOperation.text = item.address
        }
    }

}