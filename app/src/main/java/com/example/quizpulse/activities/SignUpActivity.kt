package com.example.quizpulse.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.quizpulse.screens.SignUpScreen
import com.example.quizpulse.ui.theme.QuizpulseTheme

/**
 * SignUpActivity class for the Sign Up screen.
 */
class SignUpActivity : ComponentActivity() {

    /**
     * onCreate method to set the content of the SignUpActivity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizpulseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context: Context = applicationContext
                    SignUpScreen(context, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}