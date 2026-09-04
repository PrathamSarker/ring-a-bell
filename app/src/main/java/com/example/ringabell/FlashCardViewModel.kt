package com.example.ringabell

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val _wrongCardIds = MutableStateFlow<Set<Int>>(emptySet())
    val wrongGuessCardList: StateFlow<List<FlashCard>> = combine(flashCardList, _wrongCardIds) { list, ids ->
        list.filter { it.id in ids }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var userCueInput by mutableStateOf("")
    var userAnsInput by mutableStateOf("")

    private val _correctAns = MutableStateFlow(0)
    val correctAns = _correctAns.asStateFlow()

    private val _incorrectAns = MutableStateFlow(0)
    val incorrectAns = _incorrectAns.asStateFlow()

    private val _totalAns = MutableStateFlow(0)
    val totalAns = _totalAns.asStateFlow()

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

    fun showCue(flashCard: FlashCard?): String {
        return flashCard?.Cue ?: ""
    }

    fun showAns(flashCard: FlashCard?): String {
        return flashCard?.Answer ?: ""
    }
    //Elvis operator. if flashcard is not null return answer else return empty string

    
    fun getRandomCard(): FlashCard? {
        return flashCardList.value.randomOrNull()       //if list was empty random would've broke
    }

    fun incrementTotal() {
        _totalAns.value++
    }

    fun incrementCorrect() {
        _correctAns.value++
    }

    fun incrementIncorrect() {
        _incorrectAns.value++
    }

    fun getAccuracy(correctAns: Int, totalAns: Int): Float {
        return if (totalAns == 0) 0f else (correctAns.toFloat() / totalAns.toFloat()) * 100
    }

    fun setCardIncorrect(card: FlashCard, isIncorrect: Boolean) {
        val currentIds = _wrongCardIds.value
        _wrongCardIds.value = if (isIncorrect) {
            currentIds + card.id
        } else {
            currentIds - card.id
        }
    }

    fun updateCard(oldCard: FlashCard, newCard: FlashCard) {
        viewModelScope.launch {
            dao.insertCard(newCard)
        }
    }
}
