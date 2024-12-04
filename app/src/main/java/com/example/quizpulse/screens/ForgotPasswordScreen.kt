package com.example.quizpulse.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizpulse.R
import com.example.quizpulse.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * Composable function for the "Forgot Password" screen.
 *
 * @param context The application context used for navigation and displaying messages.
 * @param modifier [Modifier] for customizing the layout or adding additional behaviors.
 */
@Composable
fun ForgotPasswordScreen(
    context: Context,
    modifier: Modifier = Modifier
) {
    // loads the animation file from resources.
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.forgot))

    var email by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Lottie Compose
        LottieAnimation(
            modifier = Modifier.size(300.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        Text(
            text = "Reset Password",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it.trim() },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        // Reset Password Button
        Button(
            onClick = {
                sendPasswordResetEmail(email, context)
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text(text = "Send Reset Link")
        }

        // Don't have an account
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString.Builder().apply {
                append("Lost your account? ")
                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    append("Sign up")
                }
            }.toAnnotatedString(),
            onClick = {
                // Navigate to the SignUpScreen
                val intent = Intent(context, SignUpActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        )
    }
}

/**
 * Sends a password reset email to the provided email address using Firebase Authentication.
 *
 * This function checks if the email address is not empty and attempts to send a password
 * reset link to the specified email. If the operation is successful, a success message is shown.
 * Otherwise, an error message is displayed. If the email field is empty, the user is prompted to
 * enter their email.
 *
 * @param email The email address to which the password reset link will be sent.
 * @param context The context of the calling component, used to display Toast messages.
 */
fun sendPasswordResetEmail(email: String, context: Context) {
    val auth = FirebaseAuth.getInstance()
    if (email.isNotEmpty()) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Reset link sent to $email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to send reset link", Toast.LENGTH_SHORT).show()
                }
            }
    } else {
        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
    }
}