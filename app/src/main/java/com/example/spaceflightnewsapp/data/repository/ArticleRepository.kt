package com.example.spaceflightnewsapp.data.repository

import com.example.spaceflightnewsapp.data.model.ArticleResponse
import com.example.spaceflightnewsapp.data.network.RetrofitClient
import retrofit2.Response

class ArticleRepository {
    suspend fun getArticles(limit: Int = 10, offset: Int = 0): ArticleResponse {
        val response = RetrofitClient.apiService.getArticles(limit, offset)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Respuesta vacía del servidor")
        } else {
            throw Exception("Error en la respuesta: código ${response.code()}")
        }
    }
}