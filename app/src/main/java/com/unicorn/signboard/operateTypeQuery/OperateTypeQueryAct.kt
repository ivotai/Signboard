package com.unicorn.signboard.operateTypeQuery

import android.annotation.SuppressLint
import com.jakewharton.rxbinding2.widget.textChanges
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.operateType.model.OperateType
import io.reactivex.Observable
import kotlinx.android.synthetic.main.title_recycler.*

@SuppressLint("CheckResult")
class OperateTypeQueryAct : BaseAct() {

    override val layoutId = R.layout.title_recycler

    private val mAdapter = OperateTypeQueryAdapter()

    override fun initViews() {
        titleBar.setTitle("经营业态")
        recyclerView.default(mAdapter)
    }

    override fun bindIntent() {
        setData()
    }

    private fun setData() {
        Observable.fromIterable(AppTime.operateTypeList)
                .distinct()
                .toList()
                .doOnSuccess { textChangeKeyword(it) }
                .subscribe { t ->
                    mAdapter.setNewData(t)
                }
    }

    private fun textChangeKeyword(allDept: List<OperateType>) {
        KeywordHeaderView(this).apply {
            setHint("请输入经营业态")
            mAdapter.addHeaderView(this)
        }.etKeyword.textChanges()
                .subscribe { keyword ->
                    allDept.filter { operateType -> operateType.name.contains(keyword) }
                            .let { mAdapter.setNewData(it) }
                }
    }

}
