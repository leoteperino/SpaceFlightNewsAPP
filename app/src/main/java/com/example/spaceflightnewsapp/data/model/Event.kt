package com.example.spaceflightnewsapp.data.model

import java.io.Serializable

data class Event(
    val event_id: Int? = null,
    val provider: String? = null
) : Serializable
