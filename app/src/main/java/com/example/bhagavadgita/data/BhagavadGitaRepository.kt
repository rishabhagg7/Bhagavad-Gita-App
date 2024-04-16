package com.example.bhagavadgita.data

import com.example.bhagavadgita.model.BhagavadGitaChapter
import com.example.bhagavadgita.model.BhagavadGitaVerse
import com.example.bhagavadgita.network.BhagavadGitaApiService

interface BhagavadGitaRepository {
    suspend fun getBhagavadGitaChapters(): List<BhagavadGitaChapter>
    suspend fun getBhagavadGitaChapter(chapterNumber:Int): BhagavadGitaChapter
    suspend fun getBhagavadGitaVerse(chapterNumber: Int,verseNumber: Int): BhagavadGitaVerse
}

class NetworkBhagavadGitaRepository(private val bhagavadGitaApiService: BhagavadGitaApiService): BhagavadGitaRepository{
    override suspend fun getBhagavadGitaChapters(): List<BhagavadGitaChapter> {
        return bhagavadGitaApiService.getChapters()
    }

    override suspend fun getBhagavadGitaChapter(chapterNumber: Int): BhagavadGitaChapter {
        return bhagavadGitaApiService.getChapter(chapterNumber)
    }

    override suspend fun getBhagavadGitaVerse(
        chapterNumber: Int,
        verseNumber: Int
    ): BhagavadGitaVerse {
        return bhagavadGitaApiService.getVerse(chapterNumber,verseNumber)
    }
}