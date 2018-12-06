package com.unicorn.signboard.summary

import android.annotation.SuppressLint
import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.summary.model.Summary
import kotlinx.android.synthetic.main.item_summary.*
import org.joda.time.DateTime

class SummaryAdapter : MyAdapter<Summary, MyHolder>(R.layout.item_summary) {

    @SuppressLint("SetTextI18n")
    override fun convert(helper: MyHolder, item: Summary) {
        helper.apply {
            tvUsername.text = "用户名: ${item.username}"
            tvMerchantCount.text = "墙面数量: ${item.merchantCount}"
            tvMerchantSignboardCount.text = "墙面招牌数量: ${item.merchantSignboardCount}"
            tvBuildingCount.text = "建筑数量: ${item.buildingCount}"
            tvGroundCount.text = "地面数量: ${item.groundCount}"
            tvLastRegistrationTime.text = "最后录入时间:\r\n${DateTime(item.lastRegistrationTime).toString("yyyy-MM-dd HH:mm:ss")}"
        }
    }

    override fun bindIntent(helper: MyHolder, viewType: Int) {
    }

}