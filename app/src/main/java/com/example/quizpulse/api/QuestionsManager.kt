package com.example.quizpulse.api

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.quizpulse.api.model.Questions
import com.example.quizpulse.db.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Manager class for fetching and managing questions from the API.
 *
 * @param database Database instance to save questions to.
 */
class QuestionsManager(private val database: AppDatabase) {
    private var _questionsResponse = mutableStateOf<List<Questions>>(emptyList())
    private var currentQuestionIndex = mutableIntStateOf(0)
    private var score = mutableIntStateOf(0)

    val questionsResponse: MutableState<List<Questions>>
        @Composable get() = remember { _questionsResponse }

    /**
     * Reset the game
     */
    fun resetGame() {
        _questionsResponse.value = emptyList()
        currentQuestionIndex.intValue = 0
        score.intValue = 0
    }

    /**
     * Get questions from the API and save them to the database
     *
     * @param category Category ID
     * @param difficulty Difficulty level
     */
    @OptIn(DelicateCoroutinesApi::class)
    fun getQuestions(category: Int, difficulty: String) {
        val service = Api.retrofitService.getQuestions(category = category, difficulty = difficulty)

        service.enqueue(object : Callback<com.example.quizpulse.api.model.Result> {
            override fun onResponse(
                call: Call<com.example.quizpulse.api.model.Result>,
                response: Response<com.example.quizpulse.api.model.Result>
            ) {
                if (response.isSuccessful) {
                    Log.i("Data", "Data is loaded successfully")

                    _questionsResponse.value = response.body()?.results ?: emptyList()
                    Log.i("DataStream", _questionsResponse.value.toString())

                    // Save data to the database
                    GlobalScope.launch {
                        saveDataToDatabase(database = database, _questionsResponse.value)
                    }
                }
            }

            override fun onFailure(call: Call<com.example.quizpulse.api.model.Result>, t: Throwable) {
                Log.e("error", "${t.message}")
            }
        })
    }

    /**
     * Save the questions to database
     */
    private fun saveDataToDatabase(database: AppDatabase, questions: List<Questions>) {
        database.questionDao().insertAllQuestions(questions)
    }

    /**
     * Get the current question
     */
    fun getCurrentQuestion(): Questions? =
        _questionsResponse.value.getOrNull(currentQuestionIndex.intValue)

    /**
     * Go to next question
     */
    fun nextQuestion() {
        if (currentQuestionIndex.intValue < _questionsResponse.value.size - 1) {
            currentQuestionIndex.intValue += 1
        }
    }
}