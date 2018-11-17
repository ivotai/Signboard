package com.unicorn.signboard.login

import com.google.gson.Gson
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.login.model.LoginResponse
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginHelper() {

     fun loginByToken() {
//        val api = ComponentHolder.appComponent.getSingleApi()
//        api.loginByToken(AppTime.loginResponse.loginToken)
//                .subscribeOn(Schedulers.io())
//                .subscribe { AppTime.loginResponse = it }
        val url = "https://kjgk.natapp4.cc/signboard/login/keep?token=${AppTime.loginResponse.loginToken}"
        val request = Request.Builder()
                .url(url)
                .build()
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        val json = response.body()!!.string()
        val gson = Gson()
        val loginResponse = gson.fromJson(json, LoginResponse::class.java)
        AppTime.loginResponse = loginResponse

    }
}