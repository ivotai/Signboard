package com.unicorn.signboard.merchant.list.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.addDefaultItemDecoration
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.app.base.PageActOrFra
import com.unicorn.signboard.app.base.PageActOrFra.Companion.rows
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.merchant.add.Merchant
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.title_swipe_recycler.*

class MerchantListAct : BaseAct(), PageActOrFra<Merchant> {

    override var mAdapter: MyAdapter<Merchant, MyHolder>? = MerchantListAdapter()

    override fun loadPage(pageNo: Int): Observable<Page<Merchant>> {
        return AppTime.api.getDict(pageNo = pageNo, pageSize = rows, lastDate = "month")
            .observeOnMain(this)
    }

    override fun initViews() {
        titleBar.setTitle("商户列表")
        super.initViews()
        mRecyclerView.addDefaultItemDecoration()
    }

    override val layoutId: Int = R.layout.title_swipe_recycler

    override val mRecyclerView: RecyclerView
        get() = recyclerView

    override val mSwipeRefreshLayout: SwipeRefreshLayout
        get() = swipeRefreshLayout

    override fun registerEvent() {
        RxBus.registerEvent(this,RefreshEvent::class.java, Consumer {
            loadFirstPage()
        })
    }

}