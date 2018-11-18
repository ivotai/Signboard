package com.unicorn.signboard.operateType.model

import com.chad.library.adapter.base.entity.IExpandable
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.operateType.OperateTypeAdapter

data class Category(
        val category: String,
        var operateTypeList: List<OperateType>? = null
) : MultiItemEntity, IExpandable<OperateType> {

    override fun getItemType() = OperateTypeAdapter.TYPE_CATEGORY

    override fun getSubItems() = if (category == "热门") AppTime.hotOperateTypeList else AppTime.operateTypeList.filter { it.category == category }

    var b = false

    override fun isExpanded() = b

    override fun getLevel() = 0

    override fun setExpanded(expanded: Boolean) {
        b = expanded
    }

}