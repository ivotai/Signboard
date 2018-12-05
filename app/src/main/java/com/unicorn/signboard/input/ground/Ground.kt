package com.unicorn.signboard.input.ground

import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.UploadResponse
import java.io.Serializable

data class Ground(
    val objectId: String = "",
    var name: String = "",
    var address: String = "",
    var area: Obj? = null,
    var operateType: Obj? = null,
    var houseNumberPicture: UploadResponse? = null,
    val houseNumberPictureLink: String = "",
    var coordinateX: Double = 0.0,
    var coordinateY: Double = 0.0,
    val signBoardList: List<GroundSignBoard> = ArrayList<GroundSignBoard>().apply { add(GroundSignBoard()) },
    val registrationTime: Long = 0
) : Serializable

data class GroundSignBoard(
    var boardHeight: Obj = AppTime.dict.SignBoardHeight[0],
    var picture: UploadResponse? = null,
    val pictureLink: String = ""
) : Serializable
