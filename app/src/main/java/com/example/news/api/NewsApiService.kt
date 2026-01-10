package com.example.news.api

import com.example.news.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 100,
        @Query("page") page: Int = 1
    ): NewsResponse
} 