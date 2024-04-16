package com.example.bhagavadgita.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class BhagavadGitaChapter(
    @SerialName(value = "chapter_number")
    val chapterNumber: Int,
    @SerialName(value = "verses_count")
    val versesCount: Int,
    val meaning: Meaning,
    val name: String,
    val summary: Summary,
    val translation: String,
    val transliteration: String,
)