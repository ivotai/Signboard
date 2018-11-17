package com.unicorn.signboard.merchant.list.ui

import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.base.Content
import kotlinx.android.synthetic.main.item_main.*

class MerchantListAdapter : MyAdapter<Content, MyHolder>(R.layout.item_main) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

    override fun convert(helper: MyHolder, item: Content) {
        helper.apply {
            tvOperation.text = item.address
        }
    }

}