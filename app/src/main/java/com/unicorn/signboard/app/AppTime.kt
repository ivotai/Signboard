package com.unicorn.signboard.app

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.unicorn.signboard.LastDate
import com.unicorn.signboard.app.api.UniqueApi
import com.unicorn.signboard.area.model.Area
import com.unicorn.signboard.login.model.LoginResponse
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.model.Dict
import com.unicorn.signboard.operateType.model.OperateType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AppTime {

    private val session: String
        get() {
            return loginResponse.session
        }

    lateinit var loginResponse: LoginResponse

    lateinit var dict: Dict

    lateinit var areaList: List<Area>

    lateinit var operateTypeList: List<OperateType>

    lateinit var hotOperateTypeList: List<OperateType>

    var lastArea: Obj? = null

    //

    lateinit var client: OkHttpClient

    lateinit var retrofit: Retrofit

    lateinit var api: UniqueApi

    val gson = Gson()

    // today今天，week本周，month本月，空或任意字符代表全部
    var lastDate = LastDate.Month

    var startDate = ""
    var endDate = ""

    fun init() {
        fun initClient() {
            client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor { chain ->
                    val pathSegments = chain.request().url().encodedPathSegments()
                    if (pathSegments.contains("login"))
                        chain.proceed(chain.request())
                    else
                        chain.request().newBuilder()
                            .addHeader(Key.cookie, "${Key.session}=${AppTime.session}")
                            .build()
                            .let { chain.proceed(it) }
                }
                .addInterceptor { chain ->
                    val response = chain.proceed(chain.request())
                    if (response.code() != 401) return@addInterceptor response
                    // when 401 一句话刷新 token
                    api.loginByToken(loginResponse.loginToken).execute().body()?.let { loginResponse = it }
                    val newRequest = chain.request().newBuilder()
                        .addHeader(Key.cookie, "${Key.session}=${AppTime.session}")
                        .build()
                    return@addInterceptor chain.proceed(newRequest)
                }
                .build()
        }
        initClient()

        fun initRetrofit() {
            retrofit = Retrofit.Builder()
                .baseUrl(ConfigUtils.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        initRetrofit()

        api = retrofit.create(UniqueApi::class.java)
    }

}

