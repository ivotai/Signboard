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
import com.afollestad.materialdialogs.MaterialDialog
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
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.util.DialogUtils
import com.unicorn.signboard.area.model.Area
import com.unicorn.signboard.area.ui.AreaAct
import com.unicorn.signboard.merchant.SignboardCountChangeEvent
import com.unicorn.signboard.operateType.model.OperateType
import com.unicorn.signboard.operateType.ui.OperateTypeAct
import com.unicorn.signboard.signboard.SignBoard
import com.unicorn.signboard.signboard.SignboardAdapter
import com.unicorn.signboard.signboard.TakeExternalDistancePhoto
import com.unicorn.signboard.signboard.TakePhotoEvent
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.act_add_merchant.*
import okhttp3.Call
import java.io.File

@SuppressLint("CheckResult")
class AddMerchantAct : BaseAct() {

    override val layoutId = R.layout.act_add_merchant

    override fun initViews() {
        titleBar.setTitle("商户录入")
        etStoreCount.setText(merchant.storeCount.toString())
        fun initOperateStatus() {
            AppTime.dict.OperateStatus.forEachIndexed { index, obj ->
                RadioButton(this).apply {
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
        fun initSignboard() {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@AddMerchantAct, LinearLayoutManager.HORIZONTAL, false)
                PagerSnapHelper().attachToRecyclerView(this)
                signboardAdapter.bindToRecyclerView(this)
            }
            signboardAdapter.setNewData(merchant.signBoardList)
            refreshSignboardCount()
        }
        initSignboard()

        val area = intent.getSerializableExtra(Key.area) as Obj?
        area?.let {
            merchant.area = it
            tvArea.text = it.name
        }
    }

    private val merchant = Merchant()
    private val signboardAdapter = SignboardAdapter()
    private lateinit var takePhotoEvent: TakePhotoEvent
    private lateinit var takeExternalDistancePhoto: TakeExternalDistancePhoto

    override fun bindIntent() {
//        tvMatchingAddress.safeClicks().subscribe { matchingAddress(etAddress.trimText()) }
//        tvMatchingName.safeClicks().subscribe { matchingName(etName.trimText()) }
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
        ivName.safeClicks().subscribe { openCamera(RequestCode.NAME) }
        tvOperateType.safeClicks().subscribe { startActivity(Intent(this, OperateTypeAct::class.java)) }
        tvArea.safeClicks().subscribe { startActivity(Intent(this, AreaAct::class.java)) }
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
            val client = LocationClient(this).apply { locOption = option }
            client.registerLocationListener(object : BDAbstractLocationListener() {
                override fun onReceiveLocation(location: BDLocation) {
                    val longitude = location.longitude      //获取经度信息
                    val latitude = location.latitude        //获取纬度信息
                    merchant.coordinateX = longitude
                    merchant.coordinateY = latitude
                    saveMerchant()
                }
            })
            client.start()
        }
    }

    private fun openCamera(requestCode: Int) {
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .compress(true)
            .forResult(requestCode)
    }

    private fun refreshSignboardCount() {
        tvSignboardCount.text = "${signboardAdapter.data.size}"
    }

    private fun matchingAddress(address: String) {
        fun render(selector: Merchant) {
            selector.apply {
                // TODO 暂时只加载地址，名称，业态，街道
                merchant.address = address
                merchant.name = name
                merchant.operateType = operateType
                merchant.area = area
                etAddress.setText(address)
                etName.setText(name)
                operateType?.name.let { tvOperateType.text = it }
                area?.name.let { tvArea.text = it }
            }
        }

        fun showMerchantListDialog(merchantList: List<Merchant>) {
            MaterialDialog.Builder(this).items(merchantList.map { it.name })
                .itemsCallback { _, _, position, _ -> render(merchantList[position]) }
                .show()
        }

        val mask = DialogUtils.showMask(this, "匹配门牌地址中...")
        AppTime.api.matchingAddress(address).observeOnMain(this).subscribeBy(
            onNext = {
                mask.dismiss()
                if (it.isEmpty()) {
                    ToastUtils.showShort("无匹配信息")
                    return@subscribeBy
                }
                showMerchantListDialog(it)
            },
            onError = {
                mask.dismiss()
            }
        )
    }

    private fun matchingName(name: String) {
        // TODO 有问题
        val mask = DialogUtils.showMask(this, "匹配商户名称中...")
        AppTime.api.matchingName(name).observeOnMain(this).subscribeBy(
            onNext = {
                mask.dismiss()
                merchant.operateType = it
                tvOperateType.text = it.name
            },
            onError = {
                mask.dismiss()
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val path = PictureSelector.obtainMultipleResult(data)[0].compressPath
            when (requestCode) {
                RequestCode.SIGNBOARD -> uploadSignboardPicture(path)
//                RequestCode.ExternalDistance -> uploadExternalDistancePhoto(path)
                else -> displayImageAndUpload(path, requestCode)
            }
        }
    }

//    private fun uploadExternalDistancePhoto(path: String) {
//        val mask = DialogUtils.showMask(this, "上传照片中...")
//        OkHttpUtils.post()
//            .addFile("attachment", path, File(path))
//            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
//            .build()
//            .execute(object : StringCallback() {
//                override fun onResponse(response: String, id: Int) {
//                    mask.dismiss()
//                    ToastUtils.showShort("上传完成")
//                    val uploadResponse = AppTime.gson.fromJson(response, UploadResponse::class.java)
//                    merchant.signBoardList[takeExternalDistancePhoto.position].externalDistancePicture = uploadResponse
//                }
//
//                override fun onError(call: Call?, e: Exception?, id: Int) {
//                    mask.dismiss()
//                }
//            })
//    }

    private fun uploadSignboardPicture(path: String) {
        val mask = DialogUtils.showMask(this, "上传招牌照片中...")
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

    private fun displayImageAndUpload(path: String, requestCode: Int) {
        when (requestCode) {
            RequestCode.ADDRESS -> Glide.with(this).load(path).into(ivAddress)
            RequestCode.NAME -> Glide.with(this).load(path).into(ivName)
        }
        OkHttpUtils.post()
            .addFile("attachment", path, File(path))
            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String, id: Int) {
                    val uploadResponse = AppTime.gson.fromJson(response, UploadResponse::class.java)
                    when (requestCode) {
                        RequestCode.ADDRESS -> merchant.houseNumberPicture = uploadResponse
                        RequestCode.NAME -> merchant.facadePicture = uploadResponse
                    }
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    when (requestCode) {
                        RequestCode.ADDRESS -> ivAddress.setProgress((progress * 100).toInt())
                        RequestCode.NAME -> ivName.setProgress((progress * 100).toInt())
                    }
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
            merchant.area = Obj(objectId = it.objectId, name = it.name)
            tvArea.text = it.name
        })
        RxBus.registerEvent(this, SignboardCountChangeEvent::class.java, Consumer {
            refreshSignboardCount()
        })
        RxBus.registerEvent(this, TakePhotoEvent::class.java, Consumer {
            takePhotoEvent = it
            openCamera(RequestCode.SIGNBOARD)
        })
//        RxBus.registerEvent(this, TakeExternalDistancePhoto::class.java, Consumer {
//            takeExternalDistancePhoto = it
//            openCamera(RequestCode.ExternalDistance)
//        })
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
            if (facadePicture == null) {
                ToastUtils.showShort("请拍摄商户名称照片")
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
                        ToastUtils.showShort("招牌照片不能为空")
                        return
                    }
                }
            }
        }
        val mask = DialogUtils.showMask(this, "保存数据中...")
        AppTime.api.saveMerchant(merchant).observeOnMain(this)
            .subscribeBy(
                onNext = {
                    mask.dismiss()
                    if (it.success) {
                        ToastUtils.showShort("保存成功")
                    } else {
                        ToastUtils.showShort(it.message)
                    }
                    startActivity(Intent(this@AddMerchantAct, AddMerchantAct::class.java).apply {
                        putExtra(Key.area, merchant.area)
                    })
                    finish()
                },
                onError = {
                    mask.dismiss()
                }
            )
    }

}