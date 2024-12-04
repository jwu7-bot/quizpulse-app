package com.example.quizpulse.api

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

/**
 * Class to handle Room database data type conversions for the 'options' field in the 'Questions' entity.
 * Uses Moshi to serialize and deserialize the list of strings.
 */
class Converters {
    /**
     * Moshi instance to serialize and deserialize JSON.
     */
    private val moshi: Moshi = Moshi.Builder().build()

    /**
     * Type of list to be serialized and deserialized.
     */
    private val listType = Types.newParameterizedType(List::class.java, String::class.java)

    /**
     * JsonAdapter to serialize and deserialize the list of strings.
     */
    private val adapter: JsonAdapter<List<String>> = moshi.adapter(listType)

    /**
     * Method to convert a list of strings to a JSON string.
     */
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.let { adapter.fromJson(it) }
    }

    /**
     * Method to convert a JSON string back to a list of strings.
     */
    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { adapter.toJson(it) }
    }
}