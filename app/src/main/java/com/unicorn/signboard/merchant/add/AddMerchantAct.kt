package com.unicorn.signboard.merchant.add

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ToastUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.unicorn.signboard.R
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
        ivAddress.safeClicks().subscribe { openCamera(RequestCode.ADDRESS) }
    }

    private fun openCamera(requestCode: Int) {
        PictureSelector.create(this)
            .openCamera(PictureMimeType.ofImage())
            .forResult(requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) return
        val path = PictureSelector.obtainMultipleResult(data)[0].path
        uploadAndDisplayPicture(requestCode, path)
    }

    private fun uploadAndDisplayPicture(requestCode: Int, path: String) {
        val file = File(path)
//        val requestBody = RequestBody.create(MediaType.parse("image/png"), file)
//        val part = MultipartBody.Part.createFormData("attachment", path, requestBody)
//        val requestOptions = RequestOptions().placeholder(R.mipmap.add_photo)

        OkHttpUtils.post()//
            .addFile("attachment", path, file)//
            .url("${ConfigUtils.baseUrl}api/v1/system/file/upload")
            .build()//
            .execute(object :StringCallback(){
                override fun onResponse(response: String, id: Int) {
                    ToastUtils.showShort(response)
                }

                override fun onError(call: Call?, e: Exception?, id: Int) {
                    ToastUtils.showShort("error")
                }
            })


//        AppTime.api.upload(part).observeOnMain(this).subscribe {
//            when (requestCode) {
//                RequestCode.ADDRESS -> {
//                    merchant.houseNumberPicture = it
//                    Glide.with(this).setDefaultRequestOptions(requestOptions).load(it.filename).into(ivAddress)
//                }
//            }
//        }
    }


}