package com.unicorn.signboard.detail.building

import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import kotlinx.android.synthetic.main.item_ground_signboard_detail.*

class BuildingSignboardDetialAdapter : MyAdapter<String, MyHolder>(R.layout.item_building_signboard_detail) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

    override fun convert(helper: MyHolder, item: String?) {
        helper.apply {
            val url = "${ConfigUtils.baseUrl2}$item"
            Glide.with(mContext).load(url).into(ivPhoto)
        }
    }

}