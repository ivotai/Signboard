package com.unicorn.signboard.area.ui

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.signboard.R
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.area.model.Area
import kotlinx.android.synthetic.main.item_text.*

class AreaAdapter : BaseQuickAdapter<Area, MyHolder>(R.layout.item_text) {

    override fun convert(helper: MyHolder, item: Area) {
        helper.apply {
            tvText.text = item.name

            tvText.safeClicks().subscribe {
                RxBus.post(item)
                val act = mContext as Activity
                act.finish()
            }
        }
    }

}