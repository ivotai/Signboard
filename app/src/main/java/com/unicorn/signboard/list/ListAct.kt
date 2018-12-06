package com.unicorn.signboard.list

import android.annotation.SuppressLint
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.unicorn.signboard.LastDate
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.safeClicks
import kotlinx.android.synthetic.main.title_tab_viewpager.*
import java.util.*


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
            val lastDates = listOf(LastDate.Today, LastDate.Week, LastDate.Month, LastDate.Total, LastDate.Range)
            MaterialDialog.Builder(this).items(lastDates.map { it.label })
                .itemsCallback { _, _, position, _ ->
                    lastDates[position].apply {
                        tvOperation.text = this.label
                        AppTime.lastDate = this
                        if (this == LastDate.Range) {
                            copeRange()
                            return@apply
                        }
                        RxBus.post(RefreshListEvent())
                    }
                }.show()
        }

        val tvOperation = titleBar.setOperation(AppTime.lastDate.label)
        tvOperation.safeClicks().subscribe { showLastDateChooseDialog(tvOperation) }
    }

    private fun copeRange() {
        val now = Calendar.getInstance()
        val dpd = DatePickerDialog.newInstance(
            { _, year, monthOfYear, dayOfMonth, yearEnd, monthOfYearEnd, dayOfMonthEnd ->
                val month = monthOfYear + 1
                val monthStr = if (month < 10) "0$month" else month.toString()
                val dayStr = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
                AppTime.startDate = "$year-$monthStr-$dayStr"

                val monthEnd = monthOfYearEnd + 1
                val monthEndStr = if (monthEnd < 10) "0$monthEnd" else monthEnd.toString()
                val dayEndStr = if (dayOfMonthEnd < 10) "0$dayOfMonthEnd" else dayOfMonthEnd.toString()
                AppTime.endDate = "$yearEnd-$monthEndStr-$dayEndStr"

                RxBus.post(RefreshListEvent())
            },
            now.get(Calendar.YEAR),
            now.get(Calendar.MONTH),
            now.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show(fragmentManager, "Datepickerdialog")
    }

}