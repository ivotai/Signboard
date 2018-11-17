package com.unicorn.signboard.app

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.jakewharton.rxbinding2.view.clicks
import com.unicorn.signboard.R
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import florent37.github.com.rxlifecycle.RxLifecycle
import florent37.github.com.rxlifecycle.RxLifecycle.disposeOnDestroy
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T, K : BaseViewHolder> RecyclerView.default(adapter:BaseQuickAdapter<T,K>){
        layoutManager = LinearLayoutManager(this.context)
        adapter.bindToRecyclerView(this)
        addDefaultItemDecoration()
}

fun View.safeClicks(lifecycleOwner: LifecycleOwner): Observable<Unit> = this.safeClicks()
        .compose(RxLifecycle.disposeOnDestroy(lifecycleOwner))

fun View.safeClicks(): Observable<Unit> = this.clicks().throttleFirst(1, TimeUnit.SECONDS)

fun TextView.trimText() = this.text.toString().trim()

fun <T> Single<T>.observeOnMain(lifecycleOwner: LifecycleOwner):Single<T> = this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(disposeOnDestroy(lifecycleOwner))

fun <T> Observable<T>.observeOnMain(lifecycleOwner: LifecycleOwner):Observable<T> = this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .compose(disposeOnDestroy(lifecycleOwner))

fun RecyclerView.addDefaultItemDecoration() {
    HorizontalDividerItemDecoration.Builder(context)
            .colorResId(R.color.md_grey_300)
            .size(1)
            .build().let { this.addItemDecoration(it) }
}

