package com.unicorn.signboard.statistics.ui

import android.content.Intent
import com.unicorn.signboard.LastDate
import com.unicorn.signboard.R
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.list.ListAct
import com.unicorn.signboard.statistics.model.Stat
import kotlinx.android.synthetic.main.item_stat.*

class StatAdapter : MyAdapter<Stat, MyHolder>(R.layout.item_stat) {

    override fun convert(helper: MyHolder, item: Stat) {
        helper.apply {
            tvType.text = item.type
            tvToday.text = item.todayCount.toString()
            tvWeek.text = item.weekCount.toString()
            tvMonth.text = item.monthCount.toString()
            tvTotal.text = item.totalCount.toString()
        }
    }

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        fun startListAct(position: Int, lastDate: LastDate) {
            Intent(mContext, ListAct::class.java).apply {
                putExtra(Key.position, position)
                putExtra(Key.lastDate, lastDate)
            }.let { mContext.startActivity(it) }
        }
        helper.apply {
            tvToday.safeClicks().subscribe {
                val pos = helper.adapterPosition
                if (pos > 0) startListAct(pos-1, LastDate.Today)
            }
            tvWeek.safeClicks().subscribe {
                val pos = helper.adapterPosition
                if (pos > 0) startListAct(pos-1, LastDate.Week)
            }
            tvMonth.safeClicks().subscribe {
                val pos = helper.adapterPosition
                if (pos > 0) startListAct(pos-1, LastDate.Month)
            }
            tvTotal.safeClicks().subscribe {
                val pos = helper.adapterPosition
                if (pos > 0) startListAct(pos-1, LastDate.Total)
            }
        }
    }

}