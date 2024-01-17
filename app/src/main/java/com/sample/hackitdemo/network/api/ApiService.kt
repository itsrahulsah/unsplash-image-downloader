package com.sample.hackitdemo.network.api

import com.sample.hackitdemo.network.models.Image
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/photos")
    suspend fun getImages(@Query("page") page: Int):List<Image>
}