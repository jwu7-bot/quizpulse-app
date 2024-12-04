package com.example.quizpulse.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.quizpulse.api.model.Questions

/**
 * DAO for interacting with the 'questions' table in the database.
 */
@Dao
interface QuestionDao {
    /**
     * Inserts all the provided questions into the 'questions' table.
     * If a question with the same id already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllQuestions(character: List<Questions>)
}