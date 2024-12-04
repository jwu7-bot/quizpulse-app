package com.example.quizpulse.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Object to manage API setup and communication for the application.
 */
object Api {
    /**
     * Base URL for the Open Trivia Database API
     */
    private val BASE_URL = "https://opentdb.com?"

    /**
     * Moshi instance for JSON serialization and deserialization,
     * configured with support for Kotlin's data classes
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Retrofit instance for making HTTP requests to the API
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Lazily initialized Retrofit service for interacting with the QuestionsServices API.
     * Ensures the Retrofit instance is created only when needed.
     */
    val retrofitService: QuestionsServices by lazy {
        retrofit.create(QuestionsServices::class.java)
    }
}