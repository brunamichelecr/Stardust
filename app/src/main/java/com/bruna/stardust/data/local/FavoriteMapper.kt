package com.bruna.stardust.data.local

import com.bruna.stardust.data.model.ApodItem

fun ApodItem.toFavorite(): FavoriteApod = FavoriteApod(
    date = date,
    title = title,
    mediaType = media_type,
    url = url,
    hdurl = hdurl,
    explanation = explanation,
    credit = credit,
    copyright = copyright
)