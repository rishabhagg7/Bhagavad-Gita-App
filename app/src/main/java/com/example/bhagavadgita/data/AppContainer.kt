package com.example.bhagavadgita.data

import com.example.bhagavadgita.network.BhagavadGitaApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val bhagavadGitaRepository: BhagavadGitaRepository
}

class DefaultAppContainer: AppContainer{
    private val BASE_URL = "https://bhagavadgitaapi.in"
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
}