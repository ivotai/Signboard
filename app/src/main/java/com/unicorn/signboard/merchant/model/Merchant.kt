package com.unicorn.signboard.merchant.model

data class Merchant(
    val address: String,
    val area: Obj,
    val coordinateX: Int,
    val coordinateY: Int,
    val name: String,
//    val objectId: String,
    val operateStatus: Obj,
    val operateType: Obj,
    val region: Obj,
    val signBoardInfoList: List<SignBoardInfo>
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
    val objectId:String,
    val name:String
)
