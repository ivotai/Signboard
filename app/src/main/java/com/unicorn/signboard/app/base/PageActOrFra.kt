package com.unicorn.signboard.app.base

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.unicorn.signboard.R
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import io.reactivex.Observable

@SuppressLint("CheckResult")
interface PageActOrFra<Model> : ActOrFra {

    val mRecyclerView: RecyclerView

    val mSwipeRefreshLayout: SwipeRefreshLayout

    // can't use late init
    var mAdapter: MyAdapter<Model, MyHolder>?

    // loadPage 需要处理线程切换以及销毁时dispose的问题
    fun loadPage(pageNo: Int): Observable<Page<Model>>

    companion object {
        val rows get() = 5
    }

    private val pageNo
        get() = mAdapter!!.data.size / rows

    override fun initViews() {
        mSwipeRefreshLayout.setOnRefreshListener { loadFirstPage() }
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        mRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            mAdapter!!.bindToRecyclerView(this)
            mAdapter!!.setEnableLoadMore(true)
            mAdapter!!.setOnLoadMoreListener({ loadNextPage() }, mRecyclerView)
        }
    }

    override fun bindIntent() {
        loadFirstPage()
    }

    fun loadFirstPage() {
        loadPage(0).subscribe({ page ->
            mSwipeRefreshLayout.isRefreshing = false
            mAdapter!!.setNewData(page.content)
            if (mAdapter!!.data.size == page.totalElements) mAdapter!!.loadMoreEnd()
        }, {
            mSwipeRefreshLayout.isRefreshing = false
        })
    }

    private fun loadNextPage() {
        loadPage(pageNo).subscribe({ page ->
            mAdapter!!.loadMoreComplete()
            mAdapter!!.addData(page.content)
            mAdapter!!.notifyDataSetChanged()
            if (mAdapter!!.data.size == page.totalElements) mAdapter!!.loadMoreEnd()
        }, {
            mAdapter!!.loadMoreComplete()
        })
    }

}