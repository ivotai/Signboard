package com.unicorn.signboard.operateType.ui

import android.annotation.SuppressLint
import android.content.Intent
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.operateType.model.Category
import com.unicorn.signboard.operateType.model.OperateType
import com.unicorn.signboard.operateTypeQuery.OperateTypeQueryAct
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.title_recycler.*

@SuppressLint("CheckResult")
class OperateTypeAct : BaseAct() {

    override val layoutId = R.layout.title_recycler

    private val mAdapter = OperateTypeAdapter()

    override fun initViews() {
        titleBar.setTitle("经营业态")
        recyclerView.default(mAdapter)
    }

    override fun bindIntent() {
        setData()
        titleBar.setOperation("查询").safeClicks().subscribe {
            startActivity(Intent(this@OperateTypeAct, OperateTypeQueryAct::class.java))
        }
    }

    private fun setData() {
        Observable.fromIterable(AppTime.operateTypeList)
            .map { Category(it.category) as MultiItemEntity }
            .distinct()
            .toList()
            .map {
                it.add(0, Category("热门"))
                return@map it
            }
            .subscribe { t -> mAdapter.setNewData(t) }
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, OperateType::class.java, Consumer {
            finish()
        })
    }
    
}
