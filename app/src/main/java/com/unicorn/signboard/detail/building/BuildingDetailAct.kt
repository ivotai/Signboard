package com.unicorn.signboard.detail.building

import com.unicorn.signboard.R
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.input.building.Building

class BuildingDetailAct:BaseAct(){

    override val layoutId = R.layout.building_detail

    private lateinit var building: Building

    override fun initViews() {
        building = intent.getSerializableExtra(Key.building) as Building
    }

    override fun bindIntent() {
    }

}