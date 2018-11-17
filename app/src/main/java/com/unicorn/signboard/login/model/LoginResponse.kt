package com.unicorn.signboard.login.model

class LoginResponse(
    val currentUser: CurrentUser,
    val session: String,
    val loginToken: String,
    success: Boolean,
    message: String
) : BaseResponse(success, message)