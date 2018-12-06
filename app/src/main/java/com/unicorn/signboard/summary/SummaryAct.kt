package com.unicorn.signboard.summary

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.borax12.materialdaterangepicker.date.DatePickerDialog
import com.jakewharton.rxbinding2.widget.textChanges
import com.unicorn.signboard.LastDate
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.app.base.PageActOrFra
import com.unicorn.signboard.list.RefreshListEvent
import com.unicorn.signboard.operateTypeQuery.KeywordHeaderView
import com.unicorn.signboard.summary.model.Summary
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.title_swipe_recycler.*
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class SummaryAct : BaseAct(), PageActOrFra<Summary> {

    override var mAdapter: MyAdapter<Summary, MyHolder>? = SummaryAdapter()

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

    private var keyword = ""

    override fun loadPage(pageNo: Int): Observable<Page<Summary>> {
        return AppTime.api.getSummary(
            page = pageNo,
            pageSize = PageActOrFra.rows,
            lastDate = AppTime.lastDate.value,
            keyword = keyword,
            startDate = AppTime.startDate,
            endDate = AppTime.endDate
        ).observeOnMain(this)
    }

    override fun initViews() {
        titleBar.setTitle("用户统计")
        super.initViews()
        mRecyclerView.addDefaultItemDecoration()
        addKeywordHeaderView()
    }

    private fun addKeywordHeaderView() {
        KeywordHeaderView(this).apply {
            setHint("请输入关键字")
            mAdapter!!.addHeaderView(this)
        }.etKeyword.textChanges().debounce(1, TimeUnit.SECONDS)
            .subscribe {
                keyword = it.toString()
                loadFirstPage()
            }
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, RefreshListEvent::class.java, Consumer {
            loadFirstPage()
        })
    }

    override val mRecyclerView: RecyclerView
        get() = recyclerView

    override val mSwipeRefreshLayout: SwipeRefreshLayout
        get() = swipeRefreshLayout

    override val layoutId = R.layout.title_swipe_recycler

}