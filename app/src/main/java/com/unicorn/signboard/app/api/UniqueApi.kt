package com.unicorn.signboard.app.api

import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.login.model.LoginParam
import com.unicorn.signboard.login.model.LoginResponse
import com.unicorn.signboard.login.model.VerifyCodeResponse
import com.unicorn.signboard.merchant.add.Merchant
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.UploadResponse
import com.unicorn.signboard.merchant.model.Dict
import com.unicorn.signboard.merchant.model.OperateType
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UniqueApi {

    // 登录
    @GET(value = "public/sms/verifyCode/login")
    fun getVerifyCode(@Query("phoneNo") phoneNo: String): Observable<VerifyCodeResponse>

    @Headers("Merchant-Type: application/json")
    @POST("login/sms")
    fun login(@Body loginInfo: LoginParam): Observable<LoginResponse>

    @GET(value = "login/keep")
    fun loginByToken(@Query("token") token: String): Call<LoginResponse>

    // 商户列表
    @GET(value = "api/v1/sign/merchant")
    fun getDict(
        @Query("pageNo") pageNo: Int,
        @Query("pageSize") pageSize: Int,
        @Query("lastDate") lastDate: String
    ): Observable<Page<Merchant>>

    //  字典等
    @GET(value = "api/v1/sign/dict")
    fun getDict(): Observable<Dict>

    @GET(value = "api/v1/sign/operateType")
    fun getOperateType(): Observable<List<OperateType>>

    @GET(value = "api/v1/sign/operateType/hot")
    fun getHotOperateType(): Observable<List<OperateType>>

    @GET(value = "api/v1/sign/area")
    fun getArea(): Observable<List<Obj>>

    // 上传
    @Multipart
    @POST("api/v1/system/file/upload")
    fun upload(@Part attachment: MultipartBody.Part): Observable<UploadResponse>


//    @GET(value = "api/v1/sign/merchant/matching")
//    fun getMerchantInfo(@Query("address") address: String): Observable<List<MerchantInfo>>
//
//    @GET(value = "api/v1/sign/operateType/matching")
//    fun matchingOperateType(@Query("keyword") keyword: String): Observable<Obj>
//
//    @Headers("Merchant-Type: application/json")
//    @POST("api/v1/sign/merchant")
//    fun saveInfo(@Body saveInfo: SaveInfo): Observable<Any>

}