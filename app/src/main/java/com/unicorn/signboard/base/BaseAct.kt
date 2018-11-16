package com.unicorn.signboard.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.mikepenz.iconics.context.IconicsLayoutInflater2

abstract class BaseAct : AppCompatActivity(), ActOrFra {

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(layoutInflater,  IconicsLayoutInflater2(delegate))
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        initViews()
        bindIntent()
    }

}