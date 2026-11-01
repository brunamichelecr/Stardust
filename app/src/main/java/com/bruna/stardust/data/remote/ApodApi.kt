package com.bruna.stardust.data.remote

import com.bruna.stardust.data.model.ApodItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApodApi {

    @GET("apod-basic?_fields=date,title,media_type,url,hdurl,explanation,credit,copyright")
    suspend fun getApod(@Query("per_page") perPage: Int): List<ApodItem>

    @GET("apod-basic/{day}?_fields=date,title,media_type,url,hdurl,explanation,credit,copyright")
    suspend fun getApodByDay(@Path("day") day: String): ApodItem
}