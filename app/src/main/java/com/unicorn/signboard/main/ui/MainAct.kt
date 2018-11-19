package com.unicorn.signboard.main.ui

import android.annotation.SuppressLint
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.app.util.DialogUtils
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.title_recycler.*

class MainAct : BaseAct() {

    override val layoutId = R.layout.title_recycler

    override fun initViews() {
        titleBar.setTitle(title = "主界面", hideBack = true)
        initRecyclerView()
    }

    private val mAdapter = MainAdapter()

    private fun initRecyclerView() {
        recyclerView.default(mAdapter)
    }

    override fun bindIntent() {
        setData()
        prepareData()
    }

    private fun setData() {
        listOf("商户录入", "商户列表", "退出").let { mAdapter.setNewData(it) }
    }

    @SuppressLint("CheckResult")
    private fun prepareData() {
        val api = AppTime.api
        val mask = DialogUtils.showMask(this, "获取数据中...")
        api.getDict()
            .flatMap {
                AppTime.dict = it
                return@flatMap api.getArea()
            }.flatMap {
                AppTime.areaList = it
                return@flatMap api.getOperateType()
            }.flatMap {
                AppTime.operateTypeList = it
                return@flatMap api.getHotOperateType()
            }.observeOnMain(this)
            .subscribeBy(
                onNext = {
                    mask.dismiss()
                    AppTime.hotOperateTypeList = it
                },
                onError = {
                    mask.dismiss()
                }
            )
    }

}
