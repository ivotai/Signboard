package com.unicorn.signboard.app.adapter

import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

abstract class MyAdapter<T, K : BaseViewHolder>(layoutId: Int) : BaseQuickAdapter<T, K>(layoutId) {

    override fun onCreateDefViewHolder(parent: ViewGroup?, viewType: Int): K {
        val helper = super.onCreateDefViewHolder(parent, viewType)
        bindIntent(helper, viewType)
        return helper
    }

    abstract fun bindIntent(helper: K, viewType: Int)

}