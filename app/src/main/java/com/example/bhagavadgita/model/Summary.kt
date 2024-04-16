package com.example.bhagavadgita.model

import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    val en: String,
    val hi: String
)