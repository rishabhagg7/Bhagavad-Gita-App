package com.example.bhagavadgita.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BhagavadGitaVerse(
    @SerialName("_id")
    val id: String,
    val abhinav: Abhinav,
    val adi: Adi,
    val anand: Anand,
    val chapter: Int,
    val chinmay: Chinmay,
    val dhan: Dhan,
    val gambir: Gambir,
    val jaya: Jaya,
    val madhav: Madhav,
    val ms: Ms,
    val neel: Neel,
    val purohit: Purohit,
    val puru: Puru,
    val raman: Raman,
    val rams: Rams,
    val san: San,
    val sankar: Sankar,
    val siva: Siva,
    val slok: String,
    val srid: Srid,
    val tej: Tej,
    val transliteration: String,
    val vallabh: Vallabh,
    val venkat: Venkat,
    val verse: Int
)