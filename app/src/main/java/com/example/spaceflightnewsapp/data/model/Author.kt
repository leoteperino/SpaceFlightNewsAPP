package com.example.spaceflightnewsapp.data.model

import java.io.Serializable

data class Author(
    val name: String? = null,
    val socials: Socials? = null
) : Serializable
