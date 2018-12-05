package com.unicorn.signboard.input.building

import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.UploadResponse
import java.io.Serializable

class Building(
    val objectId:String="",
    var name: String = "",
    var address: String = "",
    var area: Obj? = null,
    var houseNumberPicture: UploadResponse? = null,
    val houseNumberPictureLink: String = "",
    var coordinateX: Double = 0.0,
    var coordinateY: Double = 0.0,
    var number1:Int = 0,
    var number2:Int = 0,
    var number3:Int = 0,
    var number4:Int = 0,
    var number5:Int = 0,
    var number6:Int = 0,
    var number7:Int = 0,
    var number8:Int = 0,
    var number9:Int = 0,
    var number10:Int = 0,
    var number11:Int = 0,
    var number12:Int = 0,
    val pictureList: MutableList<UploadResponse?> = ArrayList<UploadResponse?>().apply { add(null) },
    val registrationTime: Long = 0
): Serializable