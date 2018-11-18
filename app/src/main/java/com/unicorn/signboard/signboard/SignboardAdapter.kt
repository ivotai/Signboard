package com.unicorn.signboard.signboard

import android.view.View
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import kotlinx.android.synthetic.main.item_signboard.*

class SignboardAdapter : MyAdapter<SignBoard, MyHolder>(R.layout.item_signboard) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

    override fun convert(helper: MyHolder, item: SignBoard) {
        helper.apply {
            val temp = item.picture
            if (temp == null)
                Glide.with(mContext).load(R.mipmap.add_photo).into(ivPhoto)
            else
                Glide.with(mContext).load(temp.filename).into(ivPhoto)
            ivDelete.visibility = if (adapterPosition == 0) View.INVISIBLE else View.VISIBLE
            tvType.text = item.type.name
            tvSetupType.text = item.setupType.name
            tvMaterial.text = item.material.name
            tvExternalDistance.text = item.externalDistance.name
            tvHeight.setText(item.height.toString())   // 高度
            tvWidth.setText(item.width.toString())     // 宽度
        }
    }

}