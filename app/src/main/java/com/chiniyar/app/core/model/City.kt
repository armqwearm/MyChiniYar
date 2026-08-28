package com.chiniyar.app.core.model

/** Stable city model shared by city UI, search and future remote/local data sources. */
data class City(
    val id: String,
    val nameZh: String,
    val nameEn: String,
    val provinceZh: String,
    val provinceEn: String,
    val descriptionFa: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val population: Long? = null,
    val isMajorMetro: Boolean = false
)
