package com.unicorn.signboard.summary

import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.summary.model.Summary

class SummaryAdapter : MyAdapter<Summary, MyHolder>(R.layout.item_right_arrow) {

    override fun convert(helper: MyHolder, item: Summary) {
    }

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

}