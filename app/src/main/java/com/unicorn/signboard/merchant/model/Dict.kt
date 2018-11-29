package com.unicorn.signboard.merchant.model

import com.unicorn.signboard.merchant.add.Obj

data class Dict(
    val SignBoardSetupType: List<Obj>,
    val SignBoardExternalDistance: List<Obj>,
    val SignBoardType: List<Obj>,
//    val SignBoardMaterial: List<Obj>,
    val OperateStatus: List<Obj>
)
