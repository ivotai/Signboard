package com.unicorn.signboard.main

import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.unicorn.signboard.R
import com.unicorn.signboard.app.MyHolder
import com.unicorn.signboard.app.safeClicks
import kotlinx.android.synthetic.main.item_main.*

class MainAdapter : BaseQuickAdapter<String, MyHolder>(R.layout.item_main) {

    override fun convert(helper: MyHolder, item: String) {
        helper.apply {
            tvOperation.text = item
        }
    }

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): MyHolder {
        val helper = super.onCreateDefViewHolder(parent, viewType)
        helper.apply {
            root.safeClicks().subscribe { ToastUtils.showShort("1") }
        }
        return helper
    }

}