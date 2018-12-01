package com.unicorn.signboard.input.ground

import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.merchant.SignboardCountChangeEvent
import kotlinx.android.synthetic.main.item_ground_signboard.*

class GroundSignboardAdapter : MyAdapter<GroundSignBoard, MyHolder>(R.layout.item_ground_signboard) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            ivDelete.safeClicks().subscribe {
                remove(adapterPosition)
                RxBus.post(SignboardCountChangeEvent())
            }
            ivPhoto.safeClicks().subscribe {
                RxBus.post(TakeGroundPhotoEvent(helper.adapterPosition))
            }
            tvBoardHeight.safeClicks().subscribe { _ ->
                MaterialDialog.Builder(mContext).items(AppTime.dict.SignBoardHeight.map { it.name })
                    .itemsCallback { _, _, position, _ ->
                        AppTime.dict.SignBoardHeight[position].apply {
                            mData[helper.adapterPosition].boardHeight = this
                            tvBoardHeight.text = name
                        }
                    }.show()
            }
        }
    }

    override fun convert(helper: MyHolder, item: GroundSignBoard) {
        helper.apply {
            val temp = item.picture
            if (temp == null)
                Glide.with(mContext).load(R.mipmap.add_photo).into(ivPhoto)
            else
                Glide.with(mContext).load(temp.filename).into(ivPhoto)
            ivDelete.visibility = if (adapterPosition == 0) View.INVISIBLE else View.VISIBLE
            tvBoardHeight.text = item.boardHeight.name
        }
    }

}