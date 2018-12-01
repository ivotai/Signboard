package com.unicorn.signboard.input.building

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseFra
import com.unicorn.signboard.app.util.DialogUtils
import com.unicorn.signboard.area.model.Area
import com.unicorn.signboard.area.ui.AreaAct
import com.unicorn.signboard.input.InputAct
import com.unicorn.signboard.merchant.SignboardCountChangeEvent
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.RequestCode
import com.unicorn.signboard.merchant.add.UploadResponse
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_building.*
import okhttp3.Call
import top.zibin.luban.Luban
import java.io.File

@SuppressLint("CheckResult")
class AddBuildingFra : BaseFra() {

    private val building = Building()
    private val buildingSignboardAdapter = BuildingSignboardAdapter()
    private lateinit var takeBuildingPhotoEvent: TakeBuildingPhotoEvent

    override fun initViews() {
        // 初始化招牌
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                buildingSignboardAdapter.bindToRecyclerView(this)
            }
            buildingSignboardAdapter.setNewData(building.pictureList)
            refreshSignboardCount()
        }
        initSignboard()
        // 初始化街道
        fun initArea() {
            AppTime.lastArea?.let {
                building.area = it
                tvArea.text = it.name
            }
        }
        initArea()
        // 初始化 number1-12
        fun initNumbers() {
            building.apply {
                tvNumber1.setText(number1.toString())
                tvNumber2.setText(number2.toString())
                tvNumber3.setText(number3.toString())
                tvNumber4.setText(number4.toString())
                tvNumber5.setText(number5.toString())
                tvNumber6.setText(number6.toString())
                tvNumber7.setText(number7.toString())
                tvNumber8.setText(number8.toString())
                tvNumber9.setText(number9.toString())
                tvNumber10.setText(number10.toString())
                tvNumber11.setText(number11.toString())
                tvNumber12.setText(number12.toString())
            }
        }
        initNumbers()
    }

    private fun refreshSignboardCount() {
        tvSignboardCount.text = "${buildingSignboardAdapter.data.size}"
    }

    override fun bindIntent() {
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
        tvArea.safeClicks().subscribe { startActivity(Intent(context, AreaAct::class.java)) }
        addSignboard.safeClicks(this).subscribe {
            buildingSignboardAdapter.data.add(null)
            refreshSignboardCount()
            recyclerView.scrollToPosition(buildingSignboardAdapter.data.size - 1)
        }
        btnSave.safeClicks().subscribe {
            val option = LocationClientOption().apply {
                setCoorType("bd09ll")
                isOpenGps = true
                isLocationNotify = true
            }
            val client = LocationClient(context).apply { locOption = option }
            client.registerLocationListener(object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation) {
                    building.coordinateX = location.longitude      //获取经度信息
                    building.coordinateY = location.latitude        //获取纬度信息
                    save()
                }
            })
            client.start()
        }
    }

    private fun openCamera(requestCode: Int) {
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .forResult(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val path = PictureSelector.obtainMultipleResult(data)[0].path
            Single.just(path)
                .observeOn(Schedulers.io())
                .map { return@map Luban.with(context).load(it).get() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { fileList ->
                    val compressPic = fileList[0]
                    val compressPicPath = compressPic.absolutePath
                    when (requestCode) {
                        RequestCode.SIGNBOARD -> uploadSignboardPic(compressPicPath)
                        else -> uploadAddressPic(compressPicPath)
                    }
                }
        }
    }

    private fun uploadSignboardPic(path: String) {
        val mask = DialogUtils.showMask(context!!, "上传招牌照片中...")
        OkHttpUtils.post()
            .addFile("attachment", path, File(path))
            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String, id: Int) {
                    mask.dismiss()
                    ToastUtils.showShort("上传完成")
                    val uploadResponse = AppTime.gson.fromJson(response, UploadResponse::class.java)
                    building.pictureList[takeBuildingPhotoEvent.position] = uploadResponse
                    buildingSignboardAdapter.notifyItemChanged(takeBuildingPhotoEvent.position)
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    mask.dismiss()
                }
            })
    }

    private fun uploadAddressPic(path: String) {
        Glide.with(this).load(path).into(ivAddress)
        OkHttpUtils.post()
            .addFile("attachment", path, File(path))
            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String, id: Int) {
                    val uploadResponse = AppTime.gson.fromJson(response, UploadResponse::class.java)
                    building.houseNumberPicture = uploadResponse
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    ivAddress.setProgress((progress * 100).toInt())
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                }
            })
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, Area::class.java, Consumer {
            val obj = Obj(objectId = it.objectId, name = it.name)
            building.area = obj
            AppTime.lastArea = obj
            tvArea.text = it.name
        })
        RxBus.registerEvent(this, SignboardCountChangeEvent::class.java, Consumer {
            refreshSignboardCount()
        })
        RxBus.registerEvent(this, TakeBuildingPhotoEvent::class.java, Consumer {
            takeBuildingPhotoEvent = it
            openCamera(RequestCode.SIGNBOARD)
        })
    }

    private fun save() {
        building.apply {
            number1 = tvNumber1.text.toString().toInt()
            number2 = tvNumber2.text.toString().toInt()
            number3 = tvNumber3.text.toString().toInt()
            number4 = tvNumber4.text.toString().toInt()
            number5 = tvNumber5.text.toString().toInt()
            number6 = tvNumber6.text.toString().toInt()
            number7 = tvNumber7.text.toString().toInt()
            number8 = tvNumber8.text.toString().toInt()
            number9 = tvNumber9.text.toString().toInt()
            number10 = tvNumber10.text.toString().toInt()
            number11 = tvNumber11.text.toString().toInt()
            number12 = tvNumber12.text.toString().toInt()
        }
        building.apply {
            address = etAddress.trimText()
            name = etName.trimText()
            if (TextUtils.isEmpty(address)) {
                ToastUtils.showShort("门牌地址不能为空")
                return
            }
            if (houseNumberPicture == null) {
                ToastUtils.showShort("请拍摄门牌地址照片")
                return
            }
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort("建筑名称不能为空")
                return
            }
            if (area == null) {
                ToastUtils.showShort("所属街道不能为空")
                return
            }
            for (pic in pictureList) {
                if (pic == null) {
                    ToastUtils.showShort("请拍摄建筑照片")
                    return
                }
            }
        }
        val mask = DialogUtils.showMask(context!!, "保存数据中...")
        AppTime.api.save(building).observeOnMain(this)
            .subscribeBy(
                onNext = {
                    mask.dismiss()
                    if (it.success) {
                        ToastUtils.showShort("保存成功")
                    } else {
                        ToastUtils.showShort(it.message)
                    }
                    startActivity(Intent(context, InputAct::class.java))
                    activity!!.finish()
                },
                onError = {
                    mask.dismiss()
                    ErrorHandler().copeError(it)
                }
            )
    }

    override val layoutId = R.layout.add_building

}