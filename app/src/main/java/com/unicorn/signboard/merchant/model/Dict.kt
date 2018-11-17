package com.unicorn.signboard.merchant.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.merchant.add.Obj

data class Dict(
    val SignBoardSetupType: List<Obj>,
    val SignBoardExternalDistance: List<Obj>,
    val SignBoardType: List<Obj>,
    val SignBoardMaterial: List<Obj>,
    val OperateStatus: List<Obj>
)

data class OperateType(
        val objectId: String,
        val name: String,
        val category: String
) : MultiItemEntity,Selector() {
        override fun getItemType() = 1
//                OperateTypeAdapter.TYPE_OPERATE_TYPE
}