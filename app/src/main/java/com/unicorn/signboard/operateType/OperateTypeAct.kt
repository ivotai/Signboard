package com.unicorn.signboard.operateType

import android.annotation.SuppressLint
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.operateType.model.Category
import io.reactivex.Observable
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

}
