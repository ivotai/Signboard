package com.unicorn.signboard.login.model

data class CheckUpdateResponse(
    val newVersion: Boolean,
    val versionNumber: String,
    val apkUrl:String
)