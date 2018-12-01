package com.unicorn.signboard.app

import com.blankj.utilcode.util.ToastUtils
import com.unicorn.signboard.login.model.BaseResponse
import retrofit2.HttpException

class ErrorHandler {

    fun copeError(throwable: Throwable) {
        if (throwable is HttpException) {
            val errorBody = throwable.response().errorBody()!!
            val baseResponse = AppTime.gson.fromJson(errorBody.string(), BaseResponse::class.java)
            if (!baseResponse.success) ToastUtils.showShort(baseResponse.message)
        }
    }

}