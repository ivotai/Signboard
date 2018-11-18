package com.unicorn.signboard.merchant.add

import com.unicorn.signboard.signboard.SignBoard

data class Merchant(
//    val objectId: String,
    var address: String = "",
    var houseNumberPicture: UploadResponse? = null,
    var name: String = "",
    var facadePicture: UploadResponse? = null,
    var operateType: Obj? = null,
    var operateStatus: Obj? = null,
    var area: Obj? = null,
    var storeCount: Int = 0,
    val region: Obj? = null,
    var coordinateX: Double = 0.0,
    var coordinateY: Double = 0.0,
    val signBoardList: List<SignBoard>? = null
)

class Obj(
    val objectId: String,
    val name: String
)
