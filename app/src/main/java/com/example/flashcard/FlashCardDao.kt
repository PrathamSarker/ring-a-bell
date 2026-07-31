package com.example.flashcard

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun getAllCards(): Flow<List<FlashCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: FlashCard)

    @Update
    suspend fun updateCard(card: FlashCard)

    @Delete
    suspend fun deleteCard(card: FlashCard)
}
