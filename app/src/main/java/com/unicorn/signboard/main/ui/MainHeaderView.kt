package com.unicorn.signboard.main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.blankj.utilcode.util.AppUtils
import com.unicorn.signboard.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.header_view_main.*

@SuppressLint("SetTextI18n")
class MainHeaderView(context: Context) : FrameLayout(context),LayoutContainer {
    override val containerView: View= this

    init {
        LayoutInflater.from(context).inflate(R.layout.header_view_main, this, true)
        tvText.text = "版本号: ${AppUtils.getAppVersionName()}"
    }

}