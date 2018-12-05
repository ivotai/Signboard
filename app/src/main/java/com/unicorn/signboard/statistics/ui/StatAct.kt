package com.unicorn.signboard.statistics.ui

import android.annotation.SuppressLint
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.statistics.model.HomeInfo
import com.unicorn.signboard.statistics.model.Stat
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.title_recycler.*

class StatAct : BaseAct() {

    override val layoutId = R.layout.title_recycler

    private val mAdapter = StatAdapter()

    override fun initViews() {
        titleBar.setTitle("商户统计")
        recyclerView.default(mAdapter)
        mAdapter.addHeaderView(StatHeaderView(this))
    }

    @SuppressLint("CheckResult")
    override fun bindIntent() {
        fun renderData(homeInfo: HomeInfo) {
            val list = ArrayList<Stat>()
            list.add(homeInfo.merchant.apply { this.type = "墙面" })
            list.add(homeInfo.building.apply { this.type = "建筑" })
            list.add(homeInfo.ground.apply { this.type = "地面" })
            mAdapter.setNewData(list)
        }

        fun getHomeInfo() {
            AppTime.api.getHomeInfo().observeOnMain(this).subscribeBy(
                onNext = { renderData(it) },
                onError = {}
            )
        }
        getHomeInfo()
    }


}
