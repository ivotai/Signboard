package com.unicorn.signboard.app

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.unicorn.signboard.app.api.UniqueApi
import com.unicorn.signboard.login.LoginHelper
import com.unicorn.signboard.login.model.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object AppTime {

    val session: String
        get() {
            return loginResponse.session
        }

    lateinit var loginResponse: LoginResponse

//    lateinit var dict: Dict

//    lateinit var operateTypeList: List<OperateType>

//    lateinit var hotOperateTypeList: List<OperateType>
    //

    lateinit var client: OkHttpClient

    lateinit var retrofit: Retrofit

    lateinit var api: UniqueApi

    fun init() {
        fun initClient() {
            client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .addInterceptor { chain ->
                    val pathSegments = chain.request().url().encodedPathSegments()
                    if (pathSegments.contains("login") || pathSegments.contains("dict"))
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
                    // when 401
                    LoginHelper().loginByToken()
                    val newRequest = chain.request().newBuilder()
                        .addHeader(Key.cookie, "${Key.session}=${AppTime.session}")
                        .build()
                    val newResponse = chain.proceed(newRequest)
                    return@addInterceptor newResponse
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

        initOther()
    }

    fun initOther(){

//        fun getDict() {
//            ComponentHolder.appComponent.getSingleApi().getDict()
//                .subscribeOn(Schedulers.io())
//                .subscribe {
//                    AppTime.dict = it
//                }
//        }
//        getDict()
    }
}

