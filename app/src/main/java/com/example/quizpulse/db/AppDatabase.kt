package com.example.quizpulse.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizpulse.api.Converters
import com.example.quizpulse.api.model.Questions

/**
 * This class provides a Room database to store and retrieve questions.
 */
@Database(entities = [Questions::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Returns an instance of the AppDatabase.
     */
    abstract fun questionDao(): QuestionDao

    /**
     * A companion object to manage the database instance.
     */
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Creates an instance of the AppDatabase if it doesn't exist, or returns the existing one.
         */
        fun getInstance(context: Context) : AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Quizpulse Project"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}