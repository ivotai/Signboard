package com.unicorn.signboard.merchant.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.unicorn.signboard.R
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.base.BaseAct
import com.unicorn.signboard.app.safeClicks
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
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
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
        ivName.safeClicks().subscribe { openCamera(RequestCode.NAME) }
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

}