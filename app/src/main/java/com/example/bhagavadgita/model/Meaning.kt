package com.example.bhagavadgita.model

import kotlinx.serialization.Serializable

@Serializable
data class Meaning(
    val en: String,
    val hi: String
)