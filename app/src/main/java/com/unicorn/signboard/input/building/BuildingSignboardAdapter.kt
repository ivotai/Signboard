package com.unicorn.signboard.input.building

import android.view.View
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.merchant.SignboardCountChangeEvent
import com.unicorn.signboard.merchant.add.UploadResponse
import kotlinx.android.synthetic.main.item_ground_signboard.*

class BuildingSignboardAdapter : MyAdapter<UploadResponse?, MyHolder>(R.layout.item_building_signboard) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            ivDelete.safeClicks().subscribe {
                remove(adapterPosition)
                RxBus.post(SignboardCountChangeEvent())
            }
            ivPhoto.safeClicks().subscribe {
                RxBus.post(TakeBuildingPhotoEvent(helper.adapterPosition))
            }
        }
    }

    override fun convert(helper: MyHolder, item: UploadResponse?) {
        helper.apply {
            if (item == null)
                Glide.with(mContext).load(R.mipmap.add_photo).into(ivPhoto)
            else
                Glide.with(mContext).load(item.filename).into(ivPhoto)
            ivDelete.visibility = if (adapterPosition == 0) View.INVISIBLE else View.VISIBLE
        }
    }

}