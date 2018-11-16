package com.unicorn.signboard.custom.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.unicorn.signboard.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.title_bar.view.*

class TitleBar(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), LayoutContainer {

    override val containerView = this

    init {
        LayoutInflater.from(context).inflate(R.layout.title_bar, this, true)
    }

    fun setTitle(title: String, hideBack: Boolean = false) {
        tvTitle.text = title
        if (hideBack) tvBack.visibility = View.INVISIBLE
    }

}