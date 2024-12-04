package com.example.quizpulse.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.quizpulse.MainActivity
import com.example.quizpulse.screens.SignInScreen
import com.example.quizpulse.ui.theme.QuizpulseTheme
import com.google.firebase.auth.FirebaseAuth

/**
 * SignInActivity handles the sign-in flow of the app
 */
class SignInActivity : ComponentActivity() {

    /**
     * On the creation of the activity, set up the content and handle the sign-in flow
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizpulseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context: Context = applicationContext
                    SignInScreen(context, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

    /**
     * Check if the user is already signed in and redirect to the main activity if so
     */
    override fun onStart() {
        super.onStart()
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            // Redirect to the main activity if already signed in
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}