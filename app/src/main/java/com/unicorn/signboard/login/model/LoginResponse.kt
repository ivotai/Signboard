package com.unicorn.signboard.login.model

data class LoginResponse(
        val currentUser: CurrentUser,
        val success: Boolean,
        val session: String,
        val loginToken: String
)