package com.example.quizpulse.api

import com.example.quizpulse.api.model.Result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for interacting with the Open Trivia Database API to fetch quiz questions.
 */
interface QuestionsServices {
    /**
     * Retrieves a list of quiz questions based on the specified parameters.
     *
     * @param amount The number of questions to fetch. Default is 10.
     * @param category The category ID for filtering questions (e.g., Science, History).
     * @param difficulty The difficulty level of the questions (e.g., "easy", "medium", "hard").
     * @param type The format of the questions. Default is "multiple" for multiple-choice questions.
     *
     * @return A [Call] object wrapping a [Result] containing the fetched questions.
     *
     * Example usage:
     * - Fetch 10 medium-difficulty science questions:
     *   `getQuestions(amount = 10, category = 17, difficulty = "medium")`
     */
    @GET("api.php")
    fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String,
        @Query("type") type: String = "multiple"
    ): Call<Result>
}
