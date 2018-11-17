package com.unicorn.signboard.main

import com.blankj.utilcode.util.ToastUtils
import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import kotlinx.android.synthetic.main.item_main.*

class MainAdapter : MyAdapter<String, MyHolder>(R.layout.item_main) {

    override fun convert(helper: MyHolder, item: String) {
        helper.apply {
            tvOperation.text = item
        }
    }

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            root.safeClicks().subscribe { ToastUtils.showShort("!") }
        }
    }

}