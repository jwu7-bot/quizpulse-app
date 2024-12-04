package com.example.quizpulse.api.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * This class represents a question retrieved from the Open Trivia Database API.
 *
 * @property id Unique identifier for the question.
 * @property category Category of the question.
 * @property correctAnswer Correct answer to the question.
 * @property difficulty Difficulty level of the question.
 */
@Entity(tableName = "questions")
@JsonClass(generateAdapter = true)
data class Questions(
    @Json(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @Json(name = "category")
    val category: String,
    @Json(name = "correct_answer")
    val correctAnswer: String,
    @Json(name = "difficulty")
    val difficulty: String,
    @Json(name = "incorrect_answers")
    val incorrectAnswers: List<String>,
    @Json(name = "question")
    val question: String,
    @Json(name = "type")
    val type: String
)