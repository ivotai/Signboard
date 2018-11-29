package com.unicorn.signboard.merchant.list.ui

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.longClicks
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.RxBus
import com.unicorn.signboard.app.adapter.MyAdapter
import com.unicorn.signboard.app.adapter.MyHolder
import com.unicorn.signboard.app.observeOnMain
import com.unicorn.signboard.merchant.add.Merchant
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.item_merchant.*
import org.joda.time.DateTime

class MerchantListAdapter : MyAdapter<Merchant, MyHolder>(R.layout.item_merchant) {

    override fun bindIntent(helper: MyHolder, viewType: Int) {
        helper.apply {
            root.longClicks().subscribe {
                MaterialDialog.Builder(mContext).title("确认删除商户？")
                    .positiveText("确认")
                    .onPositive { _, _ ->
                        val item = mData[helper.adapterPosition]
                        AppTime.api.delete(item.objectId).observeOnMain(mContext as LifecycleOwner).subscribeBy (
                            onNext = {
                                    baseResponse ->
                                if (baseResponse.success) {
                                    ToastUtils.showShort("删除成功")
                                    remove(helper.adapterPosition)
//                                    RxBus.post(RefreshEvent())
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

    override fun convert(helper: MyHolder, item: Merchant) {
        helper.apply {
            val imageUrl = "${ConfigUtils.baseUrl2}${item.facadePictureLink}!400_300"
            Glide.with(mContext).load(imageUrl).into(ivImage)
            tvName.text = item.name
            tvAddress.text = item.address
            tvRegistrationTime.text = DateTime(item.registrationTime).toString("yyyy-MM-dd HH:mm:ss")
        }
    }

}