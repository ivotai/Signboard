package com.unicorn.signboard.main

import com.unicorn.signboard.R
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
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
    }

    private fun setData() {
        listOf("商户录入","录入商户列表").let { mAdapter.setNewData(it) }
    }

}
