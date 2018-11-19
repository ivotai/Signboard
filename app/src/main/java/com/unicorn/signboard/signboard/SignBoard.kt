package com.unicorn.signboard.signboard

import com.unicorn.signboard.app.AppTime
import com.unicorn.signboard.merchant.add.Obj
import com.unicorn.signboard.merchant.add.UploadResponse

data class SignBoard(
//    val objectId: String,
    var type: Obj = AppTime.dict.SignBoardType[0],
    var setupType: Obj = AppTime.dict.SignBoardSetupType[0],
    var material: Obj = AppTime.dict.SignBoardMaterial[0],
    var externalDistance: Obj = AppTime.dict.SignBoardExternalDistance[0],
    var width: Int = 0,     // 长度
    var height: Int = 0,    // 高度
    var picture: UploadResponse? = null
)
