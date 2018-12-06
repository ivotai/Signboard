package com.unicorn.signboard.detail.building

import android.content.Intent
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.photo.PhotoAct
import kotlinx.android.synthetic.main.item_ground_signboard_detail.*

class BuildingSignboardDetialAdapter : MyAdapter<String, MyHolder>(R.layout.item_building_signboard_detail) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            ivPhoto.safeClicks().subscribe { _ ->
                Intent(mContext, PhotoAct::class.java).apply {
                    val item = mData[adapterPosition]
                    val url = "${ConfigUtils.baseUrl2}$item"
                    putExtra(Key.photoUrl, url)
                }.let { mContext.startActivity(it) }
            }
        }
    }

    override fun convert(helper: MyHolder, item: String?) {
        helper.apply {
            val url = "${ConfigUtils.baseUrl2}$item!200_200"
            Glide.with(mContext).load(url).into(ivPhoto)
        }
    }

}