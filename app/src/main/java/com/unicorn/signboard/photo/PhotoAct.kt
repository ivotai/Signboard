package com.unicorn.signboard.photo

import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.base.BaseAct
import kotlinx.android.synthetic.main.act_photo.*

class PhotoAct:BaseAct(){

    override val layoutId = R.layout.act_photo

    override fun initViews() {
        val photoUrl = intent.getStringExtra(Key.photoUrl)
        Glide.with(this).load(photoUrl).into(iv_photo)
    }

    override fun bindIntent() {
    }

}