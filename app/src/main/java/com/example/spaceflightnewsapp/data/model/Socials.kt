package com.example.spaceflightnewsapp.data.model

import java.io.Serializable

data class Socials(
    val x: String? = null,
    val youtube: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val mastodon: String? = null,
    val bluesky: String? = null
) : Serializable
