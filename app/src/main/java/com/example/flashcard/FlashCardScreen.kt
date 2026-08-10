package com.example.flashcard
import android.R.attr.text
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flashcard.ui.theme.FlashCardTheme
import androidx.compose.material3.Card
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState



//////////// BUTTONS /////////////

@Composable
fun SaveButton(onSave : () -> Unit){
    Button(onClick = onSave){
        Text(text = "Save")
    }
}


@Composable
fun DeleteButton(onDelete : () -> Unit){
    Button(onClick = onDelete){
        Text(text = "Delete")
    }
}

//////////////////////////////////

@Composable
fun FlashCardScreen(viewModel: FlashCardViewModel = viewModel(),
                    onGoToList: () -> Unit) {

    var cue by remember {mutableStateOf("") }
    var ans by remember {mutableStateOf("") }

    var error:Boolean? by remember {mutableStateOf(null)}    //error handling null

    var displayText by remember {mutableStateOf("Tap on the card to add the answer") }

    var flipped by remember {mutableStateOf(false)}

    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f     //rotation
    )


    Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
            ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp)
                .width(200.dp)
                .clickable{
                    flipped = !flipped
                }
                .graphicsLayer{
                    rotationY = rotation
                    cameraDistance = 12f * density
                },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                ) {
                if (rotation <=90f){
                    TextField(
                        value = cue,
                        onValueChange = { cue = it },
                        placeholder = { Text("Enter your Cue") },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        ),

                    )
                }
                else {
                    Box(
                        modifier = Modifier.graphicsLayer{
                            rotationY = 180f
                        }
                    ){
                        TextField(
                            value = ans,
                            onValueChange = { ans = it },
                            placeholder = { Text("Enter your Answer") },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = displayText,
            color = when(error){
                null -> MaterialTheme.colorScheme.onBackground
                true -> Color.Red
                false -> Color.Green
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row() {

            DeleteButton(
                onDelete = {
                    val flashCardList = viewModel.flashCardList.value
                    val cardToDelete = flashCardList.find { it.Cue == cue && it.Answer == ans }
                    if (cardToDelete != null) {
                        viewModel.deleteCard(cardToDelete)
                        cue = ""
                        ans = ""
                        error = false
                        displayText = "FlashCard deleted successfully!"
                        flipped = false
                        onGoToList()
                    }
                    else {
                        cue = ""
                        ans = ""
                    }
                }
            )

            Spacer(modifier = Modifier.width(16.dp))

            SaveButton(
                onSave = {
                    if (cue.isNotEmpty() && ans.isNotEmpty()) {
                        displayText = "FlashCard saved successfully!"
                        viewModel.addCard(FlashCard(Cue = cue, Answer = ans))
                        cue = ""
                        ans = ""
                        error = false
                        flipped = false
                        onGoToList()
                    } else {
                        displayText = "Please enter both Cue and Answer"
                        error = true
                    }
                }
            )
        }
    }
}
