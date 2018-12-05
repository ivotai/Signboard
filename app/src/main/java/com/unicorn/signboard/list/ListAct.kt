package com.unicorn.signboard.list

import android.annotation.SuppressLint
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.unicorn.signboard.LastDate
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.safeClicks
import kotlinx.android.synthetic.main.title_tab_viewpager.*

@SuppressLint("CheckResult")
class ListAct : BaseAct() {

    override val layoutId = R.layout.title_tab_viewpager

    override fun initViews() {
        val lastDate = intent.getSerializableExtra(Key.lastDate) as LastDate?
        lastDate?.let { AppTime.lastDate = it }

        titleBar.setTitle("商户列表")
        val position = intent.getIntExtra(Key.position, 0)
        viewPager.offscreenPageLimit = 2
        viewPager.adapter = ListPagerAdapter(fm = supportFragmentManager)
        viewPager.setCurrentItem(position, false)
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun bindIntent() {
        prepareLastDate()
    }

    private fun prepareLastDate() {
        fun showLastDateChooseDialog(tvOperation: TextView) {
            val lastDates = listOf(LastDate.Today, LastDate.Week, LastDate.Month, LastDate.Total)
            MaterialDialog.Builder(this).items(lastDates.map { it.label })
                .itemsCallback { _, _, position, _ ->
                    lastDates[position].apply {
                        tvOperation.text = this.label
                        AppTime.lastDate = this
                        RxBus.post(RefreshListEvent())
                    }
                }.show()
        }

        val tvOperation = titleBar.setOperation(AppTime.lastDate.label)
        tvOperation.safeClicks().subscribe { showLastDateChooseDialog(tvOperation) }
    }


}