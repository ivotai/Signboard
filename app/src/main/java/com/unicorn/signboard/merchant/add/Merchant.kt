package com.unicorn.signboard.merchant.add

import com.unicorn.signboard.signboard.SignBoard
import java.io.Serializable

data class Merchant(
    val objectId: String = "",
    var address: String = "",
    var houseNumberPicture: UploadResponse? = null,
    val houseNumberPictureLink: String = "",
    var name: String = "",
//    var facadePicture: UploadResponse? = null,
//    val facadePictureLink: String = "",
    var operateType: Obj? = null,
    var operateStatus: Obj? = null,
    var area: Obj? = null,
    var storeCount: Int = 1,
    val region: Obj? = null,
    var coordinateX: Double = 0.0,
    var coordinateY: Double = 0.0,
    val signBoardList: List<SignBoard> = ArrayList<SignBoard>().apply { add(SignBoard()) },
    val registrationTime: Long = 0
)

class Obj(
    val objectId: String,
    val name: String
) : Serializable
