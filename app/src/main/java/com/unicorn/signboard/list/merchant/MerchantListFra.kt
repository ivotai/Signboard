package com.unicorn.signboard.list.merchant

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.unicorn.signboard.list.RefreshListEvent
import com.unicorn.signboard.merchant.add.Merchant
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.swipe_recycler.*

class MerchantListFra : BaseFra(), PageActOrFra<Merchant> {

    override var mAdapter: MyAdapter<Merchant, MyHolder>? = MerchantListAdapter()

    override fun loadPage(pageNo: Int): Observable<Page<Merchant>> {
        return AppTime.api.getMerchant(page = pageNo, pageSize = rows, lastDate = AppTime.lastDate.value)
            .observeOnMain(this)
    }

    override fun initViews() {
        super.initViews()
        mRecyclerView.addDefaultItemDecoration()
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