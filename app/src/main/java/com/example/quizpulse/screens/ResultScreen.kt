package com.example.quizpulse.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

/**
 * Result Screen: Displays the congratulatory message and the score
 *
 * @param navController: The NavHostController to navigate to other screens
 * @param modifier: The modifier for this composable
 * @param category The category for this composable
 * @param difficulty The difficulty for this composable
 * @param score: The final score obtained by the user
 */
@Composable
fun ResultScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    category: String,
    difficulty: String,
    score: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Column for main content alignment
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // Congratulatory Text
            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Category and difficulty display Text
            Text(
                text = "Your score in $category - $difficulty",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Score display Text
            Text(
                text = "$score",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 100.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Button to retry quiz
            Button(
                onClick = {
                    navController.navigate("category") // Adjust the navigation target as needed
                },
                modifier = Modifier
                    .width(220.dp)
                    .height(55.dp)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Retry Quiz",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
