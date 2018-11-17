package com.unicorn.signboard.app.base

data class Page<T>(
    val content: List<T>,
    val empty: Boolean,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val pageable: Pageable,
    val size: Int,
    val sort: Sort,
    val totalElements: Int,
    val totalPages: Int
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class Content(
    val address: String,
    val area: Object,
    val coordinateX: Int,
    val coordinateY: Int,
    val name: String,
    val objectId: String,
    val operateStatus: Object,
    val operateType: Object,
    val region: Object,
    val signBoardInfoList: List<SignBoardInfo>
)

class Object(
    val objectId:String,
    val name:String
)

data class SignBoardInfo(
    val externalDistance: Object,
    val height: Any,
    val material: Object,
    val objectId: String,
    val picture: Any,
    val setupType: Object,
    val type: Object,
    val width: Int
)

data class Pageable(
    val offset: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val paged: Boolean,
    val sort: Sort,
    val unpaged: Boolean
)