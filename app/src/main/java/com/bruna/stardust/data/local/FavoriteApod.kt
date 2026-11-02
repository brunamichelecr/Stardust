package com.bruna.stardust.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteApod(
    @PrimaryKey val date: String,
    val title: String,
    val mediaType: String,
    val url: String,
    val hdurl: String?,
    val explanation: String?,
    val credit: String?,
    val copyright: String?,
    val note: String = "",
    val savedAt: Long = System.currentTimeMillis()
)