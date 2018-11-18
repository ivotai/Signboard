package com.unicorn.signboard.app.custom.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.unicorn.signboard.R
import com.unicorn.signboard.app.safeClicks
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.title_bar.view.*

@SuppressLint("CheckResult")
class TitleBar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), LayoutContainer {

    override val containerView = this

    init {
        LayoutInflater.from(context).inflate(R.layout.title_bar, this, true)
        tvBack.safeClicks().subscribe {
            ActivityUtils.getTopActivity().finish()
        }
    }

    fun setTitle(title: String, hideBack: Boolean = false) {
        tvTitle.text = title
        if (hideBack) tvBack.visibility = View.INVISIBLE
    }

    fun setOperation(operation: String): TextView {
        tvOperation.visibility = View.VISIBLE
        tvOperation.text = operation
        return tvOperation
    }

}