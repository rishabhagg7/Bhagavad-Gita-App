package com.example.bhagavadgita.data

import com.example.bhagavadgita.model.UserRequest
import com.example.bhagavadgita.model.UserResponse
import com.example.bhagavadgita.network.AuthApiService
import retrofit2.Response

interface AuthRepository{
    suspend fun registerUser(userRequest: UserRequest): Response<UserResponse>
}

class NetworkAuthRepository(private val authApiService: AuthApiService): AuthRepository {
    override suspend fun registerUser(userRequest: UserRequest): Response<UserResponse> {
        return authApiService.registerUser(userRequest)
    }

}