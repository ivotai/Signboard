package com.unicorn.signboard.merchant.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.widget.RadioButton
import android.widget.RadioGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.unicorn.signboard.R
import com.unicorn.signboard.app.*
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.util.DialogUitls
import com.unicorn.signboard.operateType.OperateTypeAct
import com.unicorn.signboard.operateType.model.OperateType
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
    }

    val merchant = Merchant()

    override fun bindIntent() {
        // TODO 百度地图定位
        fun openCamera(requestCode: Int) {
            PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(requestCode)
        }
        tvMatchingAddress.safeClicks().subscribe { matchingAddress(etAddress.trimText()) }
        tvMatchingName.safeClicks().subscribe { matchingName(etName.trimText()) }
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
        ivName.safeClicks().subscribe { openCamera(RequestCode.NAME) }
        fun prepareOperateStatus() {
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
        prepareOperateStatus()
        tvOperateType.safeClicks().subscribe { startActivity(Intent(this, OperateTypeAct::class.java)) }
        btnSave.safeClicks().subscribe { save() }
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

        val mask = DialogUitls.showMask(this, "匹配门牌地址中...")
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
        val mask = DialogUitls.showMask(this, "匹配商户名称中...")
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
        if (resultCode == Activity.RESULT_OK)
            displayImageAndUpload(path = PictureSelector.obtainMultipleResult(data)[0].path, requestCode = requestCode)
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
    }

    private fun save() {
        ToastUtils.showShort(merchant.toString())
    }

}