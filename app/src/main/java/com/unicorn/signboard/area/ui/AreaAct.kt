package com.unicorn.signboard.area.ui


import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.default
import kotlinx.android.synthetic.main.title_recycler.*

class AreaAct : BaseAct() {

    override val layoutId = R.layout.title_recycler

    private val mAdapter = AreaAdapter()

    override fun initViews() {
        titleBar.setTitle("选择街道")
        recyclerView.default(mAdapter)
    }

    override fun bindIntent() {
        setData()
    }

    private fun setData() {
        mAdapter.setNewData(AppTime.areaList)
    }

}
