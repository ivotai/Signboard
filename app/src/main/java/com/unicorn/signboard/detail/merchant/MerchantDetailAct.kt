package com.unicorn.signboard.detail.merchant

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.list.RefreshListEvent
import com.unicorn.signboard.merchant.add.Merchant
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.detail_merchant.*

class MerchantDetailAct : BaseAct() {

    override val layoutId = R.layout.detail_merchant

    private lateinit var merchant: Merchant

    private val mAdapter = SignboardDetailAdapter()

    override fun initViews() {
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                mAdapter.bindToRecyclerView(this)
            }
            mAdapter.setNewData(merchant.signBoardList)
        }
        fun initOperateStatus() {
            AppTime.dict.OperateStatus.forEachIndexed { index, obj ->
                RadioButton(this@MerchantDetailAct).apply {
                    id = index
                    text = obj.name
                    gravity = Gravity.CENTER
                    val padding = ConvertUtils.dp2px(16f)
                    setPadding(padding, 0, padding, 0)
                    buttonDrawable = null
                    layoutParams = RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                    )
                    // 不可编辑
                    isEnabled = false
                    segmented.addView(this)
                    if (obj.name == merchant.operateStatus?.name) {
                        isChecked = true
                    }
                }
            }
            segmented.updateBackground()
        }
        fun renderView() {
            merchant.apply {
                tvAddress.text = address
                val url = "${ConfigUtils.baseUrl2}$houseNumberPictureLink"
                Glide.with(this@MerchantDetailAct).load(url).into(ivAddress)
                tvName.text = name
                tvOperateType.text = operateType?.name
                tvArea.text = area?.name
                tvSignboardCount.text = signBoardList.size.toString()
                tvStoreCount.text = storeCount.toString()
            }
            initOperateStatus()
            initSignboard()
        }
        titleBar.setTitle("墙面详情")
        merchant = intent.getSerializableExtra(Key.merchant) as Merchant
        renderView()
    }

    @SuppressLint("CheckResult")
    override fun bindIntent() {
        btnDelete.safeClicks().subscribe {
            MaterialDialog.Builder(this).title("确认删除商户？")
                .positiveText("确认")
                .onPositive { _, _ ->
                    AppTime.api.deleteMerchant(merchant.objectId).observeOnMain(this).subscribeBy(
                        onNext = { baseResponse ->
                            if (baseResponse.success) {
                                ToastUtils.showShort("删除成功")
                                RxBus.post(RefreshListEvent())
                                finish()
                            } else {
                                ToastUtils.showShort(baseResponse.message)
                            }
                        },
                        onError = {
                        }
                    )
                }
                .build()
                .show()
        }
    }

}


