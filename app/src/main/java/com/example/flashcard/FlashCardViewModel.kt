package com.example.flashcard

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class FlashCardViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = FlashCardDatabase.getDatabase(application).flashCardDao()
    val flashCardList: StateFlow<List<FlashCard>> = dao.getAllCards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var userCueInput by mutableStateOf("")
    var userAnsInput by mutableStateOf("")

    var correctAns: Int = 0
    var incorrectAns: Int = 0
    var totalAns: Int = 0

    fun addCard(flashCard: FlashCard) {
        viewModelScope.launch {
            dao.insertCard(flashCard)
        }
        userCueInput = ""
        userAnsInput = ""
    }

    fun deleteCard(flashCard: FlashCard) {
        viewModelScope.launch {
            dao.deleteCard(flashCard)
        }
    }

    fun showCue(flashCard: FlashCard): String {
        return flashCard.Cue
    }

    fun showAns(flashCard: FlashCard): String {
        return flashCard.Answer
    }

    fun getRandomCard(flashCardList: List<FlashCard>): FlashCard {
        return flashCardList.random()
    }

    fun getAccuracy(correctAns: Int, totalAns: Int): Float {
        return if (totalAns == 0) 0f else (correctAns.toFloat() / totalAns.toFloat()) * 100
    }

    fun updateCard(oldCard: FlashCard, newCard: FlashCard) {
        viewModelScope.launch {
            dao.insertCard(newCard)
        }
    }
}
