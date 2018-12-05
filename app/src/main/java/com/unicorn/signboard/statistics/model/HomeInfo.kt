package com.unicorn.signboard.statistics.model

data class HomeInfo(
    val building: Stat,
    val ground: Stat,
    val merchant: Stat
)

data class Stat(
    var type: String,
    val todayCount: Int,
    val weekCount: Int,
    val monthCount: Int,
    val totalCount: Int
)

