package com.unicorn.signboard.list.building

import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.longClicks
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.input.building.Building
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_building.*
import org.joda.time.DateTime

class BuildingListAdapter : MyAdapter<Building, MyHolder>(R.layout.item_building) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            root.longClicks().subscribe {
                MaterialDialog.Builder(mContext).title("确认删除商户？")
                    .positiveText("确认")
                    .onPositive { _, _ ->
                        val pos = helper.adapterPosition-1
                        val item = mData[pos]
                        AppTime.api.deleteBuilding(item.objectId).observeOnMain(mContext as LifecycleOwner).subscribeBy (
                            onNext = {
                                    baseResponse ->
                                if (baseResponse.success) {
                                    ToastUtils.showShort("删除成功")
                                    remove(pos)
                                } else {
                                    ToastUtils.showShort(baseResponse.message)
                                }
                            },
                            onError = {
                                ToastUtils.showShort(it.cause.toString())
                            }
                        )
                    }
                    .build()
                    .show()
            }
        }

    }

    override fun convert(helper: MyHolder, item: Building) {
        helper.apply {
            val imageUrl = "${ConfigUtils.baseUrl2}${item.houseNumberPictureLink}!400_300"
            Glide.with(mContext).load(imageUrl).into(ivImage)
            tvName.text = item.name
            tvAddress.text = item.address
            tvRegistrationTime.text = DateTime(item.registrationTime).toString("yyyy-MM-dd HH:mm:ss")
        }
    }

}