package com.unicorn.signboard.statistics.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.unicorn.signboard.R
import kotlinx.android.extensions.LayoutContainer

class StatHeaderView(context: Context) : FrameLayout(context), LayoutContainer {

    override val containerView: View = this

    init {
        LayoutInflater.from(context).inflate(R.layout.header_stat, this, true)
    }

}