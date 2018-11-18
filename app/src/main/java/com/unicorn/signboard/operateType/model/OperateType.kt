package com.unicorn.signboard.operateType.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.operateType.OperateTypeAdapter

data class OperateType(
    val objectId: String,
    val name: String,
    val category: String
) : MultiItemEntity, Selector() {
    override fun getItemType() = OperateTypeAdapter.TYPE_OPERATE_TYPE
}