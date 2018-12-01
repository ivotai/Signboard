package com.unicorn.signboard.input

import com.unicorn.signboard.R
import com.unicorn.signboard.app.base.BaseAct
import kotlinx.android.synthetic.main.title_tab_viewpager.*

class InputAct:BaseAct(){

    override val layoutId = R.layout.title_tab_viewpager

    override fun initViews() {
        titleBar.setTitle("商户录入")
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = InputPagerAdapter(fm = supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun bindIntent() {
    }

}