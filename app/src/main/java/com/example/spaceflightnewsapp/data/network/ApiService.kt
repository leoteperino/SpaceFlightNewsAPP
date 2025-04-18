package com.example.spaceflightnewsapp.data.network

import com.example.spaceflightnewsapp.data.model.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
    suspend fun getArticles(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): Response<ArticleResponse>
}