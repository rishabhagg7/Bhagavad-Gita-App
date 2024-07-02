package com.example.bhagavadgita.network

import com.example.bhagavadgita.model.UserRequest
import com.example.bhagavadgita.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService{
    @POST("api/register")
    suspend fun registerUser(@Body userRequest: UserRequest): Response<UserResponse>
}