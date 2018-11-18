package com.unicorn.signboard.operateType

import android.app.Activity
import android.widget.TextView
import com.blankj.utilcode.util.ConvertUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.unicorn.signboard.R
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.safeClicks
import com.unicorn.signboard.operateType.model.Category
import com.unicorn.signboard.operateType.model.OperateType

class OperateTypeAdapter : BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(null) {

    companion object {
        const val TYPE_CATEGORY = 0
        const val TYPE_OPERATE_TYPE = 1
    }

    init {
        addItemType(TYPE_CATEGORY, R.layout.item_text)
        addItemType(TYPE_OPERATE_TYPE, R.layout.item_text)
    }

    override fun convert(helper: BaseViewHolder, item: MultiItemEntity) {
        when (item.itemType) {
            TYPE_CATEGORY -> {
                item as Category
                val tvText = helper.getView<TextView>(R.id.tvText)
                tvText.text = item.category
                tvText.safeClicks().subscribe {
                    if (item.isExpanded) collapse(helper.adapterPosition)
                    else expand(helper.adapterPosition)
                }
            }
            TYPE_OPERATE_TYPE -> {
                item as OperateType
                val tvText = helper.getView<TextView>(R.id.tvText)
                tvText.text = item.name

                val dp16 = ConvertUtils.dp2px(16f)
                val dp32 = dp16 * 2
                tvText.setPadding(dp32, dp16, dp16, dp16)

                tvText.safeClicks().subscribe { _ ->
                    RxBus.post(item)
                    val act = mContext as Activity
                    act.finish()
                }
            }
        }
    }

}