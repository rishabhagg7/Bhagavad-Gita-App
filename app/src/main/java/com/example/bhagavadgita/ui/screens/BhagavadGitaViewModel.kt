package com.example.bhagavadgita.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bhagavadgita.BhagavadGitaApplication
import com.example.bhagavadgita.data.BhagavadGitaRepository
import com.example.bhagavadgita.model.BhagavadGitaChapter
import com.example.bhagavadgita.model.BhagavadGitaVerse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private val bhagavadGitaVersesCount = arrayOf(
    47, // Chapter 1: Arjuna Vishada Yoga
    72, // Chapter 2: Sankhya Yoga
    43, // Chapter 3: Karma Yoga
    42, // Chapter 4: Jnana Yoga
    29, // Chapter 5: Karma Vairagya Yoga
    47, // Chapter 6: Abhyasa Yoga
    30, // Chapter 7: Paramahamsa Vijnana Yoga
    28, // Chapter 8: Aksara Brahma Yoga
    34, // Chapter 9: Raja Vidya Raja Guhya Yoga
    42, // Chapter 10: Vibhuti Yoga
    55, // Chapter 11: Visvarupa Darsana Yoga
    20, // Chapter 12: Bhakti Yoga
    35, // Chapter 13: Kshetra Kshetragna Vibhaga Yoga
    27, // Chapter 14: Gunatraya Vibhaga Yoga
    20, // Chapter 15: Purusottama Yoga
    35, // Chapter 16: Daivasura Sampad Vibhaga Yoga
    24, // Chapter 17: Sraddhatraya Vibhaga Yoga
    28, // Chapter 18: Moksha Sannyasa Yoga
)

private const val totalVerses = 700

sealed interface BhagavadGitaUiState{
    data class Success(
        val chapters: List<BhagavadGitaChapter>? = null,
        val verseOfTheDay: BhagavadGitaVerse? = null,
        val selectedChapterNumber: Int? = null,
        val verses: List<BhagavadGitaVerse>? = null
    ):BhagavadGitaUiState
    data object Error: BhagavadGitaUiState
    data object Loading: BhagavadGitaUiState
}


class BhagavadGitaViewModel(private val bhagavadGitaRepository: BhagavadGitaRepository): ViewModel() {
    var bhagavadGitaUiState: BhagavadGitaUiState by mutableStateOf(BhagavadGitaUiState.Loading)
        private set

    private lateinit var todayVerse: BhagavadGitaVerse
    private var chapters: List<BhagavadGitaChapter> = listOf()
    private var verses: List<BhagavadGitaVerse> = listOf()
    private var selectedChapterNumber = 1

    init {
        getVerseOfTheDay()
    }

     fun getChapters() {
         bhagavadGitaUiState = BhagavadGitaUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            bhagavadGitaUiState = try{
                val result = bhagavadGitaRepository.getBhagavadGitaChapters()
                chapters = result
                BhagavadGitaUiState.Success(
                    verseOfTheDay = todayVerse,
                    chapters = result,
                    verses = verses
                )
            }catch (e: IOException){
                BhagavadGitaUiState.Error
            }catch (e: HttpException){
                BhagavadGitaUiState.Error
            }
        }
     }

    fun setChapterNumber(chapterNumber: Int) {
        selectedChapterNumber = chapterNumber
        Screens.AllVerses.title = chapters[chapterNumber-1].name
        getVerses()
    }

    fun getVerses() {
        bhagavadGitaUiState = BhagavadGitaUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            bhagavadGitaUiState = try{
                val result: MutableList<BhagavadGitaVerse>  = mutableListOf()
                for(verseNumber in 1..bhagavadGitaVersesCount[selectedChapterNumber-1]){
                    result.add(bhagavadGitaRepository.getBhagavadGitaVerse(selectedChapterNumber,verseNumber))
                }
                verses = result
                BhagavadGitaUiState.Success(
                    chapters = chapters,
                    verseOfTheDay = todayVerse,
                    selectedChapterNumber = selectedChapterNumber,
                    verses = result
                )
            }catch (e: IOException){
                BhagavadGitaUiState.Error
            }catch (e: HttpException){
                BhagavadGitaUiState.Error
            }
        }
    }

     fun getVerseOfTheDay(){
        viewModelScope.launch {
            val temporaryDate = LocalDate.of(2024,4,16)
            val currentDate = LocalDate.now()
            val differenceInDays = (ChronoUnit.DAYS.between(temporaryDate,currentDate))%totalVerses
            val verseNumber: Int = ((differenceInDays%totalVerses)+1).toInt()
            val (todayChapterNumber,todayVerseNumber) = getChapterAndVerseNumber(verseNumber = verseNumber)
            bhagavadGitaUiState = BhagavadGitaUiState.Loading
            bhagavadGitaUiState = try{
                val result = bhagavadGitaRepository.getBhagavadGitaVerse(todayChapterNumber,todayVerseNumber)
                todayVerse = result
                BhagavadGitaUiState.Success(
                    verseOfTheDay = result,
                    chapters = chapters
                )
            }catch (e: IOException){
                BhagavadGitaUiState.Error
            }catch (e: HttpException){
                BhagavadGitaUiState.Error
            }
        }
    }

    private fun getChapterAndVerseNumber(
        verseNumber: Int
    ): Pair<Int,Int>{
        var reqChapterNumber = 0
        var reqVerseNumber = 0
        var currChapterNumber = 0
        var currCumulativeVerseNumber = 0
        while(currCumulativeVerseNumber < verseNumber){
            if(currCumulativeVerseNumber + bhagavadGitaVersesCount[currChapterNumber] < verseNumber){
                currCumulativeVerseNumber += bhagavadGitaVersesCount[currChapterNumber]
                currChapterNumber += 1
            }else{
                reqVerseNumber = (verseNumber - currCumulativeVerseNumber)
                reqChapterNumber = currChapterNumber+1
                currCumulativeVerseNumber = verseNumber
            }
        }
        return Pair(reqChapterNumber,reqVerseNumber)
    }



    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as BhagavadGitaApplication)
                val bhagavadGitaRepository = application.container.bhagavadGitaRepository
                BhagavadGitaViewModel(bhagavadGitaRepository = bhagavadGitaRepository)
            }
        }
    }
}