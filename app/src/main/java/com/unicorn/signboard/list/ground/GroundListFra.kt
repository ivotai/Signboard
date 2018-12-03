package com.unicorn.signboard.list.ground

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakewharton.rxbinding2.widget.textChanges
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.addDefaultItemDecoration
import com.unicorn.signboard.app.base.BaseFra
import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.app.base.PageActOrFra
import com.unicorn.signboard.app.base.PageActOrFra.Companion.rows
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.input.ground.Ground
import com.unicorn.signboard.list.RefreshListEvent
import com.unicorn.signboard.operateTypeQuery.KeywordHeaderView
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.swipe_recycler.*
import java.util.concurrent.TimeUnit

@SuppressLint("CheckResult")
class GroundListFra : BaseFra(), PageActOrFra<Ground> {

    override var mAdapter: MyAdapter<Ground, MyHolder>? = GroundListAdapter()

    private var keyword = ""

    override fun loadPage(pageNo: Int): Observable<Page<Ground>> {
        return AppTime.api.getGround(
            page = pageNo,
            pageSize = rows,
            lastDate = AppTime.lastDate.value,
            keyword = keyword
        ).observeOnMain(this)
    }

    override fun initViews() {
        super.initViews()
        mRecyclerView.addDefaultItemDecoration()
        addKeywordHeaderView()
    }

    private fun addKeywordHeaderView() {
        KeywordHeaderView(context!!).apply {
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

    override val layoutId: Int = R.layout.swipe_recycler

}