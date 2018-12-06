package com.unicorn.signboard.summary.model

data class Summary(
    val buildingCount: Int,
    val groundCount: Int,
    val lastRegistrationTime: Long,
    val merchantCount: Int,
    val merchantSignboardCount: Int,
    val phoneNo: String,
    val userId: String,
    val username: String
)