package com.unicorn.signboard.app.api

import com.unicorn.signboard.app.base.Page
import com.unicorn.signboard.area.model.Area
import com.unicorn.signboard.input.building.Building
import com.unicorn.signboard.input.ground.Ground
import com.unicorn.signboard.login.model.*
import com.unicorn.signboard.merchant.add.Merchant
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.model.Dict
import com.unicorn.signboard.operateType.model.OperateType
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*



interface UniqueApi {

    // 登录
    @GET(value = "public/sms/verifyCode/login")
    fun getVerifyCode(@Query("phoneNo") phoneNo: String): Observable<VerifyCodeResponse>

    @Headers("Content-Type: application/json")
    @POST("login/sms")
    fun login(@Body loginInfo: LoginParam): Observable<LoginResponse>

    @GET(value = "login/keep")
    fun loginByToken(@Query("token") token: String): Call<LoginResponse>

    @GET(value = "login/keep")
    fun loginByToken2(@Query("token") token: String): Observable<LoginResponse>

    // 商户列表
    @GET(value = "api/v1/sign/merchant")
    fun getMerchant(
        @Query("page") page: Int,
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
    fun getArea(): Observable<List<Area>>

    // 匹配
    @GET(value = "api/v1/sign/merchant/matching")
    fun matchingAddress(@Query("address") address: String): Observable<List<Merchant>>

    @GET(value = "api/v1/sign/operateType/matching")
    fun matchingName(@Query("keyword") keyword: String): Observable<Obj>

    // 保存
    @Headers("Content-Type: application/json")
    @POST("api/v1/sign/merchant")
    fun saveMerchant(@Body merchant: Merchant): Observable<BaseResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/sign/ground")
    fun saveGround(@Body ground:Ground): Observable<BaseResponse>

    @Headers("Content-Type: application/json")
    @POST("api/v1/sign/building")
    fun save(@Body building: Building): Observable<BaseResponse>

    // 删除 & 检查更新

    @DELETE("api/v1/sign/merchant/{objectId}")
    fun delete(@Path("objectId") objectId: String): Observable<BaseResponse>

    @GET(value = "public/checkUpdate")
    fun checkUpdate(@Query("id") id: String,@Query("version") version: String): Observable<CheckUpdateResponse>

}