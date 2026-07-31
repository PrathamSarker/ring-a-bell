package com.example.flashcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.ui.theme.FlashCardTheme

////////////    BUTTONS    ////////////

@Composable
fun YesButton(onYes: () -> Unit) {
    Button(onClick = onYes) {
        Text(text = "Yes")
    }
}

@Composable
fun NoButton(onNo: () -> Unit){
    Button(onClick = onNo){
        Text(text = "No")
    }
}

@Composable
fun NextButton(onNext: () -> Unit){
    Button(onClick = onNext){
        Text(text = "Next")
    }
}

@Composable
fun RevealAnsButton(onRevealAns: () -> Unit){
    Button(onClick = onRevealAns){
        Text(text = "Reveal Answer")
    }
}

/////////////////////////////////////


@Composable
fun GuessingCardScreen(
    cardList: List<FlashCard>,
    viewModel: FlashCardViewModel = viewModel()
) {
    var currentCard by remember { mutableStateOf(viewModel.getRandomCard(cardList)) }

    var cue = viewModel.showCue(currentCard)
    var ans = viewModel.showAns(currentCard)

    var hasAnswered by remember { mutableStateOf(false) }
    var isCorrect: Boolean? by remember { mutableStateOf(null) }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp)
                .width(200.dp),
            shape = RoundedCornerShape(8.dp)
        )  //inside this box- ans and cue
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (!hasAnswered) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = cue,
                        fontSize = 22.sp
                    )
                } else {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = ans,
                        fontSize = 22.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))   //empty space under the box

        if (!hasAnswered) {
            Text(text = "Ready to reveal the answer?")
        } else if (hasAnswered) {
            when (isCorrect) {
                true -> {
                    Text(text = "Well done! You got it right!")
                }

                false -> {
                    Text(text = "No problem! You'll get it next time!")
                }

                null -> {
                    Text(text = "Did you guess the correct answer?")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

//all the buttons
        if (!hasAnswered) {
            RevealAnsButton(onRevealAns = {
                hasAnswered = true
            })
        } else if (hasAnswered && isCorrect == null) {
            Row(modifier = Modifier) {
                NoButton(
                    onNo = {
                        isCorrect = false
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                YesButton(
                    onYes = {
                        isCorrect = true
                    }
                )
            }
        } else if (hasAnswered && isCorrect != null) {
            NextButton(
                onNext = {
                    hasAnswered = false
                    isCorrect = null

                }
            )
        }

//viewModel logic
        if (hasAnswered) {
            viewModel.totalAns++
        }
        if (isCorrect == true) {
            viewModel.correctAns++
        } else if (isCorrect == false) {
            viewModel.incorrectAns++
        }
    }
}




//
//@Preview(showBackground = true)
//@Composable
//fun GuessingCardScreenPreview() {
//    FlashCardTheme {
//        GuessingCardScreen(
//            cardList = listOf(
//                FlashCard(Cue = "What is IPv6?", Answer = "Internet Protocol version 6"),
//                FlashCard(Cue = "What is DNS?", Answer = "Domain Name System"),
//                FlashCard(Cue = "What is DHCP?", Answer = "Dynamic Host Configuration Protocol")
//            )
//        )
//    }
//}
//}