package com.example.ringabell

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ringabell.ui.theme.RingABellTheme


/////////    BUTTONS    /////

@Composable
fun EditButton(onEdit:() -> Unit){
    Button(
        onClick = onEdit){
        Text(text = "Edit")
    }
}

//SaveButton is designed in another screen
//DeleteButton is designed in another screen

@Composable
fun CancelButton(onCancel:() -> Unit){
    Button(onClick = onCancel){
        Text(text = "Cancel")
    }
}

@Composable
fun AlertBox(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    title: String,
    text: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = text) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

////////////////////////////


@Composable
fun ViewCardScreen(
    selectedCard: FlashCard,
    viewModel: FlashCardViewModel = viewModel(),
    onGoToList: () -> Unit
                   )
{

    var cue by remember(selectedCard) { mutableStateOf(selectedCard.Cue) }
    var ans by remember(selectedCard) { mutableStateOf(selectedCard.Answer) }

    var isEditing by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var flipped by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f
    )

    var displayText by remember { mutableStateOf("Tap on the card to see the answer") }

    if (showDeleteDialog) {
        AlertBox(
            onDismiss = {showDeleteDialog = false },
            onConfirm = {
                viewModel.deleteCard(selectedCard)
                showDeleteDialog = false
                onGoToList()
            },
            title = "Delete FlashCard",
            text = "Are you sure you want to delete the FlashCard?"
        )
    }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(250.dp)
                .width(200.dp)
                .clickable {
                    flipped = !flipped
                }
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 12f * density
                },
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (rotation <= 90f) {
                    TextField(
                        value = cue,
                        onValueChange = { cue = it },
                        readOnly = if (isEditing) false else true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                } else {
                    Box(
                        modifier = Modifier.graphicsLayer {
                            rotationY = 180f
                        }
                    ) {
                        TextField(
                            value = ans,
                            onValueChange = { ans = it },
                            readOnly = if (isEditing) false else true,
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

        Text(text = displayText)

        Spacer(modifier = Modifier.height(16.dp))

        Row() {
            if (!isEditing) {
                EditButton(
                    onEdit = {
                        isEditing = true
                        displayText = "Type on the card to edit"
                })

                Spacer(modifier = Modifier.width(16.dp))

                DeleteButton(onDelete = {
                    showDeleteDialog = true
                })
            } else {
                SaveButton(onSave = {
                    if (cue.isNotEmpty() && ans.isNotEmpty()) {
                        viewModel.updateCard(selectedCard, selectedCard.copy(Cue = cue, Answer = ans))
                        cue = ""
                        ans = ""
                        isEditing = false
                        displayText = "FlashCard updated successfully!"
                        onGoToList()
                    }
                })

                Spacer(modifier = Modifier.width(16.dp))

                CancelButton(onCancel = {
                    cue = selectedCard.Cue
                    ans = selectedCard.Answer
                    isEditing = false
                })
            }
        }
    }
}





//@Preview(showBackground = true)
//@Composable
//fun ViewCardScreenPreview() {
//    RingABellTheme {
//        ViewCardScreen(
//            selectedCard = FlashCard(Cue = "Capital of Bangladesh?", Answer = "Dhaka"),
//            onGoToList = {}
//        )
//    }
//}
