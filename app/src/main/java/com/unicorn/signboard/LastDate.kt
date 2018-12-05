package com.unicorn.signboard

enum class LastDate(val label: String, val value: String) {
    Today("本日", "today"),
    Week("本周", "week"),
    Month("本月", "month"),
    Total("总计", ""),
}