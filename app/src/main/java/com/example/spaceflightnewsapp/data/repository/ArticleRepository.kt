package com.example.spaceflightnewsapp.data.repository

import com.example.spaceflightnewsapp.data.model.ArticleResponse
import com.example.spaceflightnewsapp.data.network.RetrofitClient
import retrofit2.Response

class ArticleRepository {
    suspend fun getArticles(limit: Int = 10, offset: Int = 0): Response<ArticleResponse> {
        return RetrofitClient.apiService.getArticles(limit, offset)
    }
}