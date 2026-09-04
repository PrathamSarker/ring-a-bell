package com.example.ringabell

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashCardDao {
    @Query("SELECT * FROM flashcards ORDER BY id ASC")
    fun getAllCards(): Flow<List<FlashCard>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: FlashCard)

    @Delete
    suspend fun deleteCard(card: FlashCard)
}
