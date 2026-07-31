package com.example.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.ui.theme.FlashCardTheme




@Composable
fun CardListScreen(
                   viewModel: FlashCardViewModel = viewModel(),
                   onCardClick: (FlashCard) -> Unit,
                   onCreateNewCard: () -> Unit,
                   onStartPractice: () -> Unit) {

    val flashCardList by viewModel.flashCardList.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .safeDrawingPadding()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Your FlashCard List",
            fontSize = 23.sp
        )

        Spacer(modifier = Modifier.height(29.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(flashCardList) { card ->
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
            NewCardFAB(onCreateNewCard = onCreateNewCard)

            Spacer(modifier = Modifier.width(60.dp))

            StartPracticeFAB(onStartFAB = onStartPractice)
        }
    }
}


///////////////         BUTTONS    ///////////////////
@Composable
fun NewCardFAB(onCreateNewCard: () -> Unit){
    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        onClick = { onCreateNewCard() }
    ) {
        Text(modifier=Modifier.padding(14.dp),
            text = "Add New Card")
    }
}


@Composable
fun StartPracticeFAB(onStartFAB: () -> Unit){
    FloatingActionButton(
        modifier = Modifier.padding(16.dp),
        onClick = {onStartFAB()}
    ){
        Text(modifier=Modifier.padding(13.dp),
            text = "Start Practice")
    }
}

////////////////////////////////////////////////////

//
//@Preview(showBackground = true)
//@Composable
//fun NewCardFABPreview(){
//    FlashCardTheme{
//        NewCardFAB(onCreateNewCard = {})
//    }
//}
//
//
