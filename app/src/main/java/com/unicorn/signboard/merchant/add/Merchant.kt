package com.unicorn.signboard.merchant.add

data class Merchant(
    val address: String = "",
    var houseNumberPicture: UploadResponse? = null,
    val name: String = "",
    var facadePicture: UploadResponse? = null,
    val area: Obj? = null,
    var coordinateX: Double = 0.0,
    var coordinateY: Double = 0.0,
//    val objectId: String,
    val operateStatus: Obj? = null,
    val operateType: Obj? = null,
    val region: Obj? = null,
    val signBoardList: List<SignBoardInfo>? = null
)

data class SignBoardInfo(
    val externalDistance: Obj,
    val height: Any,
    val material: Obj,
    val objectId: String,
    val picture: Any,
    val setupType: Obj,
    val type: Obj,
    val width: Int
)

class Obj(
    val objectId: String,
    val name: String
)
