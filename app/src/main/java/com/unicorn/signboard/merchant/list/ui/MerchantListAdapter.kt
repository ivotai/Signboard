package com.unicorn.signboard.merchant.list.ui

import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.merchant.add.Merchant
import kotlinx.android.synthetic.main.item_merchant.*
import org.joda.time.DateTime

class MerchantListAdapter : MyAdapter<Merchant, MyHolder>(R.layout.item_merchant) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

    override fun convert(helper: MyHolder, item: Merchant) {
        helper.apply {
            val imageUrl = "${ConfigUtils.baseUrl}${item.facadePictureLink}!400_300"
            Glide.with(mContext).load(imageUrl).into(ivImage)
            tvName.text = item.name
            tvAddress.text = item.address
            tvRegistrationTime.text = DateTime(item.registrationTime).toString("yyyy-MM-dd HH:mm:ss")
        }
    }

}