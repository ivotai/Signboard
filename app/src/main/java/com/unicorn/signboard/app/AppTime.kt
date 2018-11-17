package com.unicorn.signboard.app

import com.unicorn.signboard.login.model.LoginResponse

object AppTime {

    val session: String
        get() {
            return loginResponse.session
        }

    lateinit var loginResponse: LoginResponse

//    lateinit var dict: Dict

//    lateinit var operateTypeList: List<OperateType>

//    lateinit var hotOperateTypeList: List<OperateType>

}

