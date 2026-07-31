package com.example.flashcard


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class FlashCard (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val Cue: String,
    val Answer: String
)