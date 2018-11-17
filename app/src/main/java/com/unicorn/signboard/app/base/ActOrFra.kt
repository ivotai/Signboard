package com.unicorn.signboard.app.base

interface ActOrFra {

    val layoutId: Int

    fun initViews()

    fun bindIntent()

    fun registerEvent()

}