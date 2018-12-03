package com.unicorn.signboard.merchant.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.widget.textChanges
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
import com.unicorn.signboard.operateType.model.OperateType
import com.unicorn.signboard.operateType.ui.OperateTypeAct
import com.unicorn.signboard.signboard.SignBoard
import com.unicorn.signboard.signboard.SignboardAdapter
import com.unicorn.signboard.signboard.TakePhotoEvent
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_merchant.*
import okhttp3.Call
import top.zibin.luban.Luban
import java.io.File

@SuppressLint("CheckResult")
class AddMerchantFra : BaseFra() {

    private val merchant = Merchant()
    private val signboardAdapter = SignboardAdapter()
    private lateinit var takePhotoEvent: TakePhotoEvent

    override fun initViews() {
        // 初始化经营状态
        fun initOperateStatus() {
            AppTime.dict.OperateStatus.forEachIndexed { index, obj ->
                RadioButton(context).apply {
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
                    segmented.addView(this)
                }
            }
            segmented.updateBackground()
            segmented.setOnCheckedChangeListener { _, checkedId ->
                merchant.operateStatus = AppTime.dict.OperateStatus[checkedId]
            }
            segmented.check(0)
        }
        initOperateStatus()
        // 初始化店家数量
        etStoreCount.setText(merchant.storeCount.toString())
        // 初始化招牌
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                signboardAdapter.bindToRecyclerView(this)
            }
            signboardAdapter.setNewData(merchant.signBoardList)
            refreshSignboardCount()
        }
        initSignboard()
        // 初始化街道
        fun initArea() {
            AppTime.lastArea?.let {
                merchant.area = it
                tvArea.text = it.name
            }
        }
        initArea()
    }

    private fun refreshSignboardCount() {
        tvSignboardCount.text = "${signboardAdapter.data.size}"
    }

    override fun bindIntent() {
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
        tvMatchingName.safeClicks().subscribe { matchingName(etName.trimText()) }
        tvOperateType.safeClicks().subscribe { startActivity(Intent(context, OperateTypeAct::class.java)) }
        tvArea.safeClicks().subscribe { startActivity(Intent(context, AreaAct::class.java)) }
        etStoreCount.textChanges().filter { it.isNotEmpty() }.map { it.toString().toInt() }
            .subscribe { merchant.storeCount = it }
        addSignboard.safeClicks(this).subscribe {
            signboardAdapter.addData(SignBoard())
            refreshSignboardCount()
            recyclerView.scrollToPosition(signboardAdapter.data.size - 1)
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
                    merchant.coordinateX = location.longitude      //获取经度信息
                    merchant.coordinateY = location.latitude        //获取纬度信息
                    saveMerchant()
                }
            })
            client.start()
        }
    }

    private fun matchingName(name: String) {
        val mask = DialogUtils.showMask(context!!, "匹配商户名称中...")
        AppTime.api.matchingName(name).observeOnMain(this).subscribeBy(
            onNext = {
                mask.dismiss()
                merchant.operateType = it
                tvOperateType.text = it.name
            },
            onError = {
                mask.dismiss()
                ToastUtils.showShort("未匹配到相关经营业态")
            }
        )
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
                    merchant.signBoardList[takePhotoEvent.position].picture = uploadResponse
                    signboardAdapter.notifyItemChanged(takePhotoEvent.position)
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
                    merchant.houseNumberPicture = uploadResponse
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    ivAddress.setProgress((progress * 100).toInt())
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                }
            })
    }

    override fun registerEvent() {
        RxBus.registerEvent(this, OperateType::class.java, Consumer {
            merchant.operateType = Obj(objectId = it.objectId, name = it.name)
            tvOperateType.text = it.name
        })
        RxBus.registerEvent(this, Area::class.java, Consumer {
            val obj = Obj(objectId = it.objectId,name = it.name)
            merchant.area=obj
            AppTime.lastArea = obj
            tvArea.text = it.name
        })
        RxBus.registerEvent(this, SignboardCountChangeEvent::class.java, Consumer {
            refreshSignboardCount()
        })
        RxBus.registerEvent(this, TakePhotoEvent::class.java, Consumer {
            takePhotoEvent = it
            openCamera(RequestCode.SIGNBOARD)
        })
    }

    private fun saveMerchant() {
        merchant.apply {
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
                ToastUtils.showShort("商户名称不能为空")
                return
            }
            if (operateType == null) {
                ToastUtils.showShort("经营业态不能为空")
                return
            }
            if (area == null) {
                ToastUtils.showShort("所属街道不能为空")
                return
            }
            for (signboard in signBoardList) {
                signboard.apply {
                    if (picture == null) {
                        ToastUtils.showShort("请拍摄招牌照片")
                        return
                    }
                }
            }
        }
        val mask = DialogUtils.showMask(context!!, "保存数据中...")
        AppTime.api.saveMerchant(merchant).observeOnMain(this)
            .subscribeBy(
                onNext = {
                    mask.dismiss()
                    if (it.success) {
                        ToastUtils.showShort("保存成功")
                    } else {
                        ToastUtils.showShort(it.message)
                    }
                    startActivity(Intent(context, InputAct::class.java).apply {
                        putExtra(Key.area, merchant.area)
                    })
                    activity!!.finish()
                },
                onError = {
                    mask.dismiss()
                    ErrorHandler().copeError(it)
                }
            )
    }

    override val layoutId = R.layout.add_merchant

}
