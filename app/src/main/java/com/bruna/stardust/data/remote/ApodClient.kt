package com.bruna.stardust.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApodClient {

    private val json = Json { ignoreUnknownKeys = true }

    val api: ApodApi = Retrofit.Builder()
        .baseUrl("https://science.nasa.gov/wp-json/wp/v2/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApodApi::class.java)
}