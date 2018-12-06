package com.unicorn.signboard.summary

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.app.base.PageActOrFra
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.summary.model.Summary
import io.reactivex.Observable
import kotlinx.android.synthetic.main.title_swipe_recycler.*

class SummaryAct : BaseAct(), PageActOrFra<Summary> {

    override var mAdapter: MyAdapter<Summary, MyHolder>? = SummaryAdapter()

    override fun initViews() {
    }

    override fun bindIntent() {
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

    override val mRecyclerView: RecyclerView
        get() = recyclerView

    override val mSwipeRefreshLayout: SwipeRefreshLayout
        get() = swipeRefreshLayout

    override val layoutId = R.layout.title_swipe_recycler

}