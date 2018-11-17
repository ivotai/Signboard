package com.unicorn.signboard.app.net

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.unicorn.signboard.app.api.UniqueApi
import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.app.ConfigUtils
import com.unicorn.signboard.app.Key
import com.unicorn.signboard.login.LoginHelper
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(ConfigUtils.baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideSingleApi(retrofit: Retrofit): UniqueApi = retrofit.create(UniqueApi::class.java)

}