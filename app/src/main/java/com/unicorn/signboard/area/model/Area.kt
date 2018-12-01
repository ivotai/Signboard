package com.unicorn.signboard.area.model

import java.io.Serializable

data class Area(
        val objectId: String,
        val name: String,
        val region: String
): Serializable