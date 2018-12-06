package com.unicorn.signboard.detail.ground

import android.annotation.SuppressLint
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.input.ground.Ground
import com.unicorn.signboard.list.RefreshListEvent
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.detail_ground.*

class GroundDetailAct : BaseAct() {

    override val layoutId = R.layout.detail_ground

    lateinit var ground: Ground

    private val mAdapter = GroundSignboardDetailAdapter()

    override fun initViews() {
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                mAdapter.bindToRecyclerView(this)
            }
            mAdapter.setNewData(ground.signBoardList)
        }
        fun renderView() {
            ground.apply {
                tvAddress.text = address
                val url = "${ConfigUtils.baseUrl2}$houseNumberPictureLink!100_100"
                Glide.with(this@GroundDetailAct).load(url).into(ivAddress)
                tvName.text = name
                tvOperateType.text = operateType?.name
                tvArea.text = area?.name
                tvSignboardCount.text = signBoardList.size.toString()
            }
            initSignboard()
        }
        ground = intent.getSerializableExtra(Key.ground) as Ground
        titleBar.setTitle("地面详情")
        renderView()
    }

    @SuppressLint("CheckResult")
    override fun bindIntent() {
        btnDelete.safeClicks().subscribe {
            MaterialDialog.Builder(this).title("确认删除商户？")
                .positiveText("确认")
                .onPositive { _, _ ->
                    AppTime.api.deleteGround(ground.objectId).observeOnMain(this).subscribeBy(
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