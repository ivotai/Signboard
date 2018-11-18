package com.unicorn.signboard.signboard

import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.UploadResponse

data class SignBoard(
//    val objectId: String,
    val type: Obj = AppTime.dict.SignBoardType[0],
    val setupType: Obj = AppTime.dict.SignBoardSetupType[0],
    val material: Obj = AppTime.dict.SignBoardMaterial[0],
    val externalDistance: Obj = AppTime.dict.SignBoardExternalDistance[0],
    val width: Int = 0,     // 长度
    val height: Int = 0,    // 高度
    val picture: UploadResponse? = null
)
