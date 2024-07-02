package com.example.bhagavadgita.model

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Int,
    val token: String
)