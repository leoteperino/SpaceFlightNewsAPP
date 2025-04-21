package com.example.spaceflightnewsapp.data.model

import java.io.Serializable

data class Article(
    val id: Int? = null,
    val title: String? = null,
    val authors: List<Author>? = emptyList(),
    val url: String? = null,
    val image_url: String? = null,
    val news_site: String? = null,
    val summary: String? = null,
    val published_at: String? = null,
    val updated_at: String? = null,
    val featured: Boolean? = null,
    val launches: List<Launch>? = emptyList(),
    val events: List<Event>? = emptyList()
) : Serializable
