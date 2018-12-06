package com.unicorn.signboard.detail.ground

import android.content.Intent
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.input.ground.GroundSignBoard
import com.unicorn.signboard.photo.PhotoAct
import kotlinx.android.synthetic.main.item_ground_signboard_detail.*

class GroundSignboardDetailAdapter : MyAdapter<GroundSignBoard, MyHolder>(R.layout.item_ground_signboard_detail) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            ivPhoto.safeClicks().subscribe { _ ->
                Intent(mContext, PhotoAct::class.java).apply {
                    val item = mData[adapterPosition]
                    val url = "${ConfigUtils.baseUrl2}${item.pictureLink}"
                    putExtra(Key.photoUrl, url)
                }.let { mContext.startActivity(it) }
            }
        }
    }

    override fun convert(helper: MyHolder, item: GroundSignBoard) {
        helper.apply {
            val url = "${ConfigUtils.baseUrl2}${item.pictureLink}!200_200"
            Glide.with(mContext).load(url).into(ivPhoto)
            tvBoardHeight.text = item.boardHeight.name
        }
    }

}