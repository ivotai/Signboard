package com.unicorn.signboard.main.ui

import android.content.Intent
import com.unicorn.signboard.R
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.input.InputAct
import com.unicorn.signboard.login.ui.LoginAct
import com.unicorn.signboard.merchant.add.AddMerchantAct
import com.unicorn.signboard.merchant.list.ui.MerchantListAct
import kotlinx.android.synthetic.main.item_right_arrow.*

class MainAdapter : MyAdapter<String, MyHolder>(R.layout.item_right_arrow) {

    override fun convert(helper: MyHolder, item: String) {
        helper.apply {
            tvOperation.text = item
        }
    }

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            root.safeClicks().subscribe {
                when (helper.adapterPosition) {
                    0 -> mContext.startActivity(Intent(mContext, InputAct::class.java))
                    1 -> mContext.startActivity(Intent(mContext, MerchantListAct::class.java))
                    2 -> {
                        mContext.startActivity(Intent(mContext, LoginAct::class.java).apply {
                            putExtra(Key.logout,true)
                        })
                        val act = mContext as BaseAct
                        act.finish()
                    }
                }
            }
        }
    }


}