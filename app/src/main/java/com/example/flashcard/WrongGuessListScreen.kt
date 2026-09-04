package com.example.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun WrongGuessListScreen(
                   viewModel: FlashCardViewModel = viewModel(),
                   onCardClick: (FlashCard) -> Unit,
                   onBack: () -> Unit) {

    val wrongGuessCardList by viewModel.wrongGuessCardList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Wrong Guess List",
            fontSize = 23.sp
        )

        Spacer(modifier = Modifier.height(29.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(wrongGuessCardList) { card ->
                Card(modifier = Modifier
                    .padding(8.dp)
                    .width(150.dp)
                    .clickable { onCardClick(card) }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = card.Cue
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            
            //reusing FAB style but calling onBack for "Review & Improve" flow back navigation
            BackToPracticeFAB(onBack = onBack)
        }
    }
}


///////////////    BUTTONS    ///////////////////

@Composable
fun BackToPracticeFAB(onBack: () -> Unit){
    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        onClick = { onBack() }
    ) {
        Text(modifier=Modifier.padding(14.dp),
            text = "Back to Practice")
    }
}

/////////////////////////////////////////////////
