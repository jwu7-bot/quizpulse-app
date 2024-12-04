package com.example.quizpulse.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from the Open Trivia Database API
 *
 * @property responseCode The HTTP status code of the response
 * @property results A list of questions and their corresponding information
 */
@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "response_code")
    val responseCode: Int,
    @Json(name = "results")
    val results: List<Questions>
)