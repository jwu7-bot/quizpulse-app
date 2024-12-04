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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.quizpulse.R
import com.example.quizpulse.activities.SignInActivity
import com.google.firebase.auth.FirebaseAuth

/**
 * SignUpScreen composes the Sign Up screen with email, password, and confirm password fields.
 *
 * @param context The context of the current activity.
 * @param modifier The modifier for the screen, defaults to Modifier.fillMaxSize(1f)
 */
@Composable
fun SignUpScreen(
    context: Context,
    modifier: Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.signup))

    // State variables
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) } // Toggle for password visibility
    var isConfirmPasswordVisible by remember { mutableStateOf(false) } // Toggle for confirm password visibility
    val keyboardController = LocalSoftwareKeyboardController.current

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
            text = "Create your account",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
            modifier = Modifier
                .padding(bottom = 10.dp)
        )

        // Email TextField
        TextField(
            value = email,
            onValueChange = { email = it.trim() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Email)
        )

        // Password TextField with Icon
        TextField(
            value = password,
            onValueChange = { password = it.trim() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Filled.Lock else Icons.Filled.Lock,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    )
                }
            }
        )

        // Confirm Password TextField with Icon
        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it.trim() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Confirm Password") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Password),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Filled.Lock else Icons.Filled.Lock,
                        contentDescription = if (isConfirmPasswordVisible) "Hide confirm password" else "Show confirm password"
                    )
                }
            }
        )

        // Sign Up Button
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                    if (keyboardController != null) {
                        performsSignUp(email, password, context, keyboardController)
                    }
                } else {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(text = "Sign Up")
        }

        // Already have an account? Login Link
        Spacer(modifier = Modifier.height(16.dp))
        ClickableText(
            text = AnnotatedString.Builder().apply {
                append("Already have an account? ")
                withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                    append("Sign in")
                }
            }.toAnnotatedString(),
            onClick = {
                // Navigate to the SignInScreen
                val intent = Intent(context, SignInActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyMedium.copy(textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        )
    }
}

/**
 * Handles user sign-up using Firebase Authentication.
 *
 * This function creates a new user account with the provided email and password.
 * On successful sign-up, the user is redirected to the sign-in screen. If the sign-up fails,
 * an error message is displayed. The software keyboard is hidden at the end of the process.
 *
 * @param email The email address provided by the user for sign-up.
 * @param password The password provided by the user for the account.
 * @param context The context of the calling component, used for showing Toast messages and starting activities.
 * @param keyboardController The [SoftwareKeyboardController] to hide the keyboard after the action is complete.
 */
fun performsSignUp(
    email: String,
    password: String,
    context: Context,
    keyboardController: SoftwareKeyboardController
) {
    val auth = FirebaseAuth.getInstance()
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Sign Up Success", Toast.LENGTH_SHORT).show()
                // Proceed to the next sign in activity to sign in
                val intent = Intent(context, SignInActivity::class.java)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
            keyboardController.hide()
        }
}
