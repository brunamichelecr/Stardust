package com.bruna.stardust.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ApodItem(
    val date: String,
    val title: String,
    val media_type: String,
    val url: String,
    val hdurl: String? = null,
    val explanation: String? = null,
    val credit: String? = null,
    val copyright: String? = null
)