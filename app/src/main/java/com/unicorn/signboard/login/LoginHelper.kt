package com.unicorn.signboard.login

import com.google.gson.Gson
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.login.model.LoginResponse
import okhttp3.Request

class LoginHelper {

    fun loginByToken() {
        val url = "${ConfigUtils.baseUrl}login/keep?token=${AppTime.loginResponse.loginToken}"
        val request = Request.Builder()
            .url(url)
            .build()
        val client = AppTime.client
        val response = client.newCall(request).execute()
        val json = response.body()!!.string()
        val gson = Gson()
        val loginResponse = gson.fromJson(json, LoginResponse::class.java)
        AppTime.loginResponse = loginResponse
    }

}