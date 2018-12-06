package com.unicorn.signboard.detail.building

import android.annotation.SuppressLint
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.input.building.Building
import com.unicorn.signboard.list.RefreshListEvent
import com.unicorn.signboard.photo.PhotoAct
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.detail_building.*

@SuppressLint("CheckResult")
class BuildingDetailAct : BaseAct() {

    override val layoutId = R.layout.detail_building

    private lateinit var building: Building

    private val mAdapter = BuildingSignboardDetialAdapter()

    override fun initViews() {
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                mAdapter.bindToRecyclerView(this)
            }
            mAdapter.setNewData(building.pictureLinks)
        }
        fun renderView() {
            building.apply {
                tvAddress.text = address
                val url = "${ConfigUtils.baseUrl2}$houseNumberPictureLink!100_100"
                Glide.with(this@BuildingDetailAct).load(url).into(ivAddress)
                tvName.text = name
                tvArea.text = area?.name
                tvNumber1.text = number1.toString()
                tvNumber2.text = number2.toString()
                tvNumber3.text = number3.toString()
                tvNumber4.text = number4.toString()
                tvNumber5.text = number5.toString()
                tvNumber6.text = number6.toString()
                tvNumber7.text = number7.toString()
                tvNumber8.text = number8.toString()
                tvNumber9.text = number9.toString()
                tvNumber10.text = number10.toString()
                tvNumber11.text = number11.toString()
                tvNumber12.text = number12.toString()
            }
            initSignboard()
        }
        building = intent.getSerializableExtra(Key.building) as Building
        titleBar.setTitle("建筑详情")
        renderView()
    }

    override fun bindIntent() {
        ivAddress.safeClicks().subscribe { _ ->
            Intent(this@BuildingDetailAct, PhotoAct::class.java).apply {
                val url = "${ConfigUtils.baseUrl2}${building.houseNumberPictureLink}"
                putExtra(Key.photoUrl, url)
            }.let { startActivity(it) }
        }
        btnDelete.safeClicks().subscribe {
            MaterialDialog.Builder(this).title("确认删除商户？")
                .positiveText("确认")
                .onPositive { _, _ ->
                    AppTime.api.deleteBuilding(building.objectId).observeOnMain(this).subscribeBy(
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