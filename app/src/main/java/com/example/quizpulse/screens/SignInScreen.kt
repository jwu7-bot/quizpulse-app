package com.example.quizpulse.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizpulse.activities.ForgotPasswordActivity
import com.example.quizpulse.activities.MainActivity
import com.example.quizpulse.R
import com.example.quizpulse.activities.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * SignInScreen is a composable function that represents the Sign In screen.
 * It takes a context as a parameter and uses it to fetch resources.
 *
 * @param context The application context
 * @param modifier The modifier for the screen, defaults to Modifier.fillMaxSize(1f)
 */
@Composable
fun SignInScreen(
    context: Context,
    modifier: Modifier = Modifier
){
    // loads the animation file from resources.
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.brain))

// State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) } // Toggle state for password visibility
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize(1f)
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
            text = "Welcome to QuizPulse!",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
            modifier = Modifier
               .padding(bottom = 10.dp)
        )

        // Email Input
        TextField(
            value = email,
            onValueChange = { email = it.trim() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
        )

        // Password Input
        TextField(
            value = password,
            onValueChange = { password = it.trim() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Lock else Icons.Default.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        // Sign-In Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    if (keyboardController != null) {
                        performsSignIn(email, password, context, keyboardController)
                    }
                } else {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Sign In")
        }

        // Forgot Password Link
        Text(
            text = "Forgot Password?",
            modifier = Modifier.clickable {
                // Navigate to ForgotPasswordScreen
                val intent = Intent(context, ForgotPasswordActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium
        )

        // Create an Account Link
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString.Builder().apply {
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    append("Create an account")
                }
            }.toAnnotatedString(),
            onClick = {
                // Navigate to the SignUpScreen
                val intent = Intent(context, SignUpActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

/**
 * Handles user sign-in using Firebase Authentication.
 *
 * This function attempts to sign in a user with the provided email and password.
 * On successful sign-in, the user is redirected to the main activity. If the sign-in fails,
 * an error message is displayed. The software keyboard is hidden at the end of the process.
 *
 * @param email The email address provided by the user for sign-in.
 * @param password The password provided by the user for the account.
 * @param context The context of the calling component, used for showing Toast messages and starting activities.
 * @param keyboardController The [SoftwareKeyboardController] to hide the keyboard after the action is complete.
 */
private fun performsSignIn(
    email: String,
    password: String,
    context: Context,
    keyboardController: SoftwareKeyboardController
) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign In Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userID", auth.currentUser?.uid)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Sign In Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
            keyboardController.hide()
        }
}
