package com.unicorn.signboard.signboard

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
import kotlinx.android.synthetic.main.item_signboard.*

class SignboardAdapter : MyAdapter<SignBoard, MyHolder>(R.layout.item_signboard) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            ivDelete.safeClicks().subscribe {
                remove(adapterPosition)
                RxBus.post(SignboardCountChangeEvent())
            }
            ivPhoto.safeClicks().subscribe {
                RxBus.post(TakePhotoEvent(helper.adapterPosition))
            }
            tvType.safeClicks().subscribe {
                MaterialDialog.Builder(mContext).items(AppTime.dict.SignBoardType.map { it.name })
                    .itemsCallback { _, _, position, _ ->
                        AppTime.dict.SignBoardType[position].apply {
                            mData[helper.adapterPosition].type = this
                            tvType.text = name
                            // 如果选择不是门头店招
                            if (this.name != "门头店招"){
                                mData[helper.adapterPosition].setupType = AppTime.dict.SignBoardSetupType[0]
                                notifyItemChanged(helper.adapterPosition)
                            }
                        }
                    }.show()
            }
            tvSetupType.safeClicks().subscribe {
                MaterialDialog.Builder(mContext).items(AppTime.dict.SignBoardSetupType.map { it.name })
                    .itemsCallback { _, _, position, _ ->
                        AppTime.dict.SignBoardSetupType[position].apply {
                            mData[helper.adapterPosition].setupType = this
                            tvSetupType.text = name
                        }
                    }.show()
            }
            tvStructure.safeClicks().subscribe {
                MaterialDialog.Builder(mContext).items(listOf("有", "无"))
                    .itemsCallback { _, _, position, _ ->
                        mData[helper.adapterPosition].structure = listOf(1, 0)[position]
                        tvStructure.text = listOf("有", "无")[position]
                    }.show()
            }
            tvExternalDistance.safeClicks().subscribe {
                MaterialDialog.Builder(mContext).items(AppTime.dict.SignBoardExternalDistance.map { it.name })
                    .itemsCallback { _, _, position, _ ->
                        AppTime.dict.SignBoardExternalDistance[position].apply {
                            mData[helper.adapterPosition].externalDistance = this
                            tvExternalDistance.text = name
                        }
                    }.show()
            }
        }
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
            tvStructure.text = if (item.structure == 1) "有" else "无"
            tvExternalDistance.text = item.externalDistance.name
        }
    }

}