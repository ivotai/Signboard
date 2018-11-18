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
        preparePhoto()
    }

    private fun preparePhoto() {
        fun openCamera(requestCode: Int) {
            PictureSelector.create(this)
                .openCamera(PictureMimeType.ofImage())
                .forResult(requestCode)
        }
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val path = PictureSelector.obtainMultipleResult(data)[0].path
        uploadImage(path = path, requestCode = requestCode)
    }

    private fun uploadImage(path: String, requestCode: Int) {
        Glide.with(this).load(path).into(ivAddress)
        OkHttpUtils.post()
            .addFile("attachment", path, File(path))
            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
            .build()
            .execute(object : StringCallback() {
                override fun onResponse(response: String, id: Int) {
                    val uploadResponse = AppTime.gson.fromJson(response, UploadResponse::class.java)
                    copeResponse(uploadResponse, requestCode)
                }

                override fun inProgress(progress: Float, total: Long, id: Int) {
                    ivAddress.setProgress((progress * 100).toInt())
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                }
            })
    }

    private fun copeResponse(uploadResponse: UploadResponse, requestCode: Int) {
        when (requestCode) {
            RequestCode.ADDRESS -> merchant.houseNumberPicture = uploadResponse
        }
    }

}