package com.example.bhagavadgita.data

import com.example.bhagavadgita.network.AuthApiService
import com.example.bhagavadgita.network.BhagavadGitaApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bhagavadGitaRepository: BhagavadGitaRepository
    val authRepository: AuthRepository
}

class DefaultAppContainer: AppContainer{
    private val BASE_URL = "https://vedicscriptures.github.io/"
    private val retrofit = Retrofit
        .Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: BhagavadGitaApiService by lazy {
        retrofit.create(BhagavadGitaApiService::class.java)
    }
    override val bhagavadGitaRepository: BhagavadGitaRepository by lazy {
        NetworkBhagavadGitaRepository(retrofitService)
    }
    private val BASE_URL_AUTH = "https://reqres.in"
    private  val retrofitAuth = Retrofit
        .Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL_AUTH)
        .build()
    private  val retrofitServiceAuth:AuthApiService by lazy{
        retrofitAuth.create(AuthApiService::class.java)
    }
    override val authRepository: AuthRepository by lazy {
        NetworkAuthRepository(retrofitServiceAuth)
    }

}