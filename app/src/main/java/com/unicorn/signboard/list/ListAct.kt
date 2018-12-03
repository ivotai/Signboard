package com.unicorn.signboard.list

import com.unicorn.signboard.R
import com.unicorn.signboard.app.base.BaseAct
import kotlinx.android.synthetic.main.title_tab_viewpager.*

class ListAct : BaseAct() {

    override val layoutId = R.layout.title_tab_viewpager

    override fun initViews() {
        titleBar.setTitle("商户列表")
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = ListPagerAdapter(fm = supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun bindIntent() {
    }

}