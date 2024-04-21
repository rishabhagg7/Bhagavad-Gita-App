package com.example.bhagavadgita.ui.screens

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.bhagavadgita.data.BhagavadGitaRepository
import com.example.bhagavadgita.model.BhagavadGitaVerse

class VersesPagingSource(
    private val bhagavadGitaRepository: BhagavadGitaRepository,
    private val chapterNumber: Int,
    private val totalVerses: Int
): PagingSource<Int, BhagavadGitaVerse>() {
    override fun getRefreshKey(state: PagingState<Int, BhagavadGitaVerse>): Int =
        ((state.anchorPosition ?: 0) - state.config.initialLoadSize / 2)
            .coerceAtLeast(0)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BhagavadGitaVerse> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response =
                bhagavadGitaRepository.getBhagavadGitaVerse(chapterNumber, nextPageNumber)
            LoadResult.Page(
                data = listOf(response),
                prevKey = if(nextPageNumber == 1) null else nextPageNumber-1,
                nextKey = if(nextPageNumber == totalVerses) null else nextPageNumber+1
            )
        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

}