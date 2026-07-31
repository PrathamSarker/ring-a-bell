package com.example.flashcard

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun FlashCardApp(viewModel: FlashCardViewModel = viewModel()){
    var currentScreen by remember { mutableStateOf("list")}
    var selectedCard by remember { mutableStateOf<FlashCard?>(null) }

    when(currentScreen){
        "createNewCard" -> {
            FlashCardScreen(viewModel = viewModel,
                onGoToList = {
                    currentScreen = "list"
                })
        }
        "viewCard" -> {
        if (selectedCard != null) {
            ViewCardScreen(
                selectedCard = selectedCard!!,
                viewModel = viewModel,
                onGoToList = {
                    currentScreen = "list"
                }
            )
        }
        }
        "list" -> {
            CardListScreen(
                viewModel = viewModel,
                onCardClick = { card ->
                    selectedCard = card
                    currentScreen = "viewCard"
                },
                onCreateNewCard = {
                    currentScreen = "createNewCard"
                },
                onStartPractice = {
                    currentScreen = "practice"
                }
            )
        }
        "practice" -> {
            val flashCardList by viewModel.flashCardList.collectAsState()
            GuessingCardScreen(
                cardList = flashCardList,
                viewModel = viewModel
            )
        }
    }

    BackHandler(enabled = (currentScreen != "not_gonna_work")) {
        when (currentScreen) {
            "createNewCard" -> currentScreen = "list"
            "practice" -> currentScreen = "list"
            "list" -> currentScreen = "not_gonna_work"
        }
    }

}

