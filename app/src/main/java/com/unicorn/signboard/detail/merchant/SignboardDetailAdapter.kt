package com.unicorn.signboard.detail.merchant

import android.content.Intent
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.photo.PhotoAct
import com.unicorn.signboard.signboard.SignBoard
import kotlinx.android.synthetic.main.item_signboard_detail.*

class SignboardDetailAdapter : MyAdapter<SignBoard, MyHolder>(R.layout.item_signboard_detail) {

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

    override fun convert(helper: MyHolder, item: SignBoard) {
        helper.apply {
            val url = "${ConfigUtils.baseUrl2}${item.pictureLink}!200_200"
            Glide.with(mContext).load(url).into(ivPhoto)
            tvType.text = item.type.name
            tvSetupType.text = item.setupType.name
            tvStructure.text = if (item.structure == 1) "有" else "无"
            tvExternalDistance.text = item.externalDistance.name
        }
    }

}