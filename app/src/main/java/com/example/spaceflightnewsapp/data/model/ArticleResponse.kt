package com.example.spaceflightnewsapp.data.model

data class ArticleResponse(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Article>? = emptyList()
)
