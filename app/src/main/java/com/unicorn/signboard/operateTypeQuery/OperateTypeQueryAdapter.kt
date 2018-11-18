package com.unicorn.signboard.operateTypeQuery

import android.app.Activity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.signboard.R
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.operateType.model.OperateType
import kotlinx.android.synthetic.main.item_text.*

class OperateTypeQueryAdapter : BaseQuickAdapter<OperateType, MyHolder>(R.layout.item_text) {

    override fun convert(helper: MyHolder, item: OperateType) {
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