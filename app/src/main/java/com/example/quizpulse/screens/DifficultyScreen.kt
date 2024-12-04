package com.example.quizpulse.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Screen to select the difficulty level for the quiz
 *
 * @param navController The navigation controller for navigating to other screens
 * @param modifier The modifier for the screen
 * @param category The category for which the quiz is being played
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DifficultyScreen(
    navController: NavController,
    modifier: Modifier,
    category: String
) {
    // List of difficulties to display
    val difficulties = listOf(
        "Easy",
        "Medium",
        "Hard"
    )

    var selectedDifficulty by remember { mutableStateOf<String?>(null) }

    // Layout for the difficulties cards
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title or header text
        Text(
            text = "Select a Difficulty",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the difficulties as a list of cards
        LazyColumn {
            items(difficulties.size) { index ->
                DifficultyCard(
                    difficulty = difficulties[index],
                    isSelected = difficulties[index] == selectedDifficulty,
                    onClick = { selectedDifficulty = difficulties[index] }
                )
            }
        }

        if (selectedDifficulty?.isNotEmpty() == true) {
            Button(
                onClick = {
                    // Pass category and difficulty to QuizScreen
                    if (selectedDifficulty != null) {
                        navController.navigate("quiz/${category}/${selectedDifficulty!!.lowercase()}")
                    }
                },
                enabled = selectedDifficulty != null,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 50.dp)
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Start Quiz", fontSize = 23.sp)
            }
        }
    }
}

/**
 * Card for displaying a difficulty level
 *
 * @param difficulty The difficulty level
 * @param isSelected Whether the difficulty is currently selected
 * @param onClick The click handler for the difficulty card
 * @return The difficulty card component
 */
@Composable
fun DifficultyCard(
    difficulty: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    // Card representing a difficulty
    Card(
        modifier = Modifier
            .width(350.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Text(
                text = difficulty,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}