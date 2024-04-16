package com.example.bhagavadgita.network

import com.example.bhagavadgita.model.BhagavadGitaChapter
import com.example.bhagavadgita.model.BhagavadGitaVerse
import retrofit2.http.GET
import retrofit2.http.Path

interface BhagavadGitaApiService{
    @GET("chapters")
    suspend fun getChapters(): List<BhagavadGitaChapter>

    @GET("chapter/{chapter_number}")
    suspend fun getChapter(@Path("chapter_number") chapterNumber: Int): BhagavadGitaChapter

    @GET("slok/{chapter_number}/{verse_number}")
    suspend fun getVerse(
        @Path("chapter_number") chapterNumber: Int,
        @Path("verse_number") verseNumber: Int
    ): BhagavadGitaVerse
}
