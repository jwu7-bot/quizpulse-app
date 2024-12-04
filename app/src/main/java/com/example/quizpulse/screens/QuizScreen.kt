package com.example.quizpulse.screens

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.navigation.NavHostController
import com.example.quizpulse.api.QuestionsManager
import com.example.quizpulse.utils.categoryMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Quiz Screen with multiple-choice questions
 *
 * @param navController Navigation controller for navigation between screens
 * @param modifier Modifier for the screen
 * @param questionManager QuestionsManager for managing questions
 * @param category Category of the quiz
 * @param difficulty Difficulty of the quiz
 * @param fs_db FirebaseDatabase
 */
@Composable
fun QuizScreen(
    navController: NavHostController,
    modifier: Modifier,
    questionManager: QuestionsManager,
    category :String,
    difficulty : String,
    fs_db: FirebaseFirestore
) {
    // Reset score, question and question index when starting a new game
    LaunchedEffect(Unit) {
        questionManager.resetGame()
    }

    // Questions from the question manager
    val questions = questionManager.questionsResponse.value
    val currentQuestion = questionManager.getCurrentQuestion()

    // Current question index in the list
    var currentQuestionIndex by remember { mutableIntStateOf(0) }

    // Score tracking
    var score by remember { mutableIntStateOf(0) }

    // Convert category name to ID
    val categoryId = categoryMap[category] ?: 9

    // State to track the selected answer and its correctness
    val selectedAnswer = remember { mutableStateOf<String?>(null) }
    val isSubmitted = remember { mutableStateOf(false) }

    // Get questions in category and difficulty
    LaunchedEffect(category, difficulty) {
        questionManager.getQuestions(categoryId, difficulty)
    }

    // Mix correct and incorrect answers
    val allAnswers = remember { mutableStateListOf<String>() }
    LaunchedEffect(currentQuestion) {
        if (currentQuestion != null) {
            allAnswers.clear()
            allAnswers.add(currentQuestion.correctAnswer)
            allAnswers.addAll(currentQuestion.incorrectAnswers)
            allAnswers.shuffle() // Randomize options
            selectedAnswer.value = null
            isSubmitted.value = false
        }
    }

    // Format the questions to correct display
    val questionText = if (questions.isNotEmpty()) {
        val rawText = currentQuestion?.question.orEmpty()
        HtmlCompat.fromHtml(rawText, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    } else {
        "Loading question..."
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the question text
            Text(
                text = questionText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 1.dp),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Progress indicator text
            Text(
                text = "Question ${currentQuestionIndex + 1} of ${questions.size}",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            // Progress indicator
            LinearProgressIndicator(
                progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display all the options
            allAnswers.forEach { option ->
                // Card that change stroke when selected
                Card(
                    modifier = Modifier
                        .width(350.dp)
                        .padding(vertical = 8.dp)
                        .clickable {
                            // Assign selected option
                            if (!isSubmitted.value) {
                                selectedAnswer.value = option
                            }
                        },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 3.dp,
                        color =
                        when {
                            // Change the stroke color to green if correct or red otherwise
                            isSubmitted.value && option == currentQuestion?.correctAnswer -> Color.Green
                            isSubmitted.value && option == selectedAnswer.value -> Color.Red
                            selectedAnswer.value == option -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.surface
                        }
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = HtmlCompat.fromHtml(option, HtmlCompat.FROM_HTML_MODE_LEGACY).toString(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        // Submit button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (selectedAnswer.value != null) {
                Button(
                    onClick = {
                        if (!isSubmitted.value) {
                            // Enable submit button when answer is selected
                            isSubmitted.value = true

                            // Update the score if the answer is correct
                            if (selectedAnswer.value == currentQuestion?.correctAnswer) {
                                score++
                            }
                        } else {
                            if (currentQuestionIndex < questions.size - 1) {
                                // Update the questionIndex
                                currentQuestionIndex++
                            } else {
                                // Go to result screen when all questions are answered
                                navController.navigate("resultScreen/${category}/${difficulty}/${score}")
                            }
                            // Go to next question
                            questionManager.nextQuestion()

                            // Reset state after navigation
                            selectedAnswer.value = null
                        }

                        val userScore = hashMapOf(
                            "userId" to FirebaseAuth.getInstance().currentUser?.email,
                            "category" to category,
                            "difficulty" to difficulty,
                            "score" to score
                        )

                        fs_db.collection("userScore")
                            .add(userScore)
                            .addOnSuccessListener { documentReference ->
                                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                            }
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                    enabled = selectedAnswer.value != null
                ) {
                    Text(
                        // Show submit button when option is selected
                        text = if (isSubmitted.value) "Next" else "Submit",
                        fontSize = 23.sp,
                    )
                }
            }

            // Skip button when answer is not submitted
            if (!isSubmitted.value)
            {
                // Skip button
                Button(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            // Update the questionIndex
                            currentQuestionIndex++
                        } else {
                            // Go to result screen when all questions are answered
                            navController.navigate("resultScreen/${category}/${difficulty}/${score}")
                        }
                        // Go to next question
                        questionManager.nextQuestion()

                        // Reset state after navigation
                        selectedAnswer.value = null
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp),
                )
                {
                    Text(text = "Skip", fontSize = 23.sp)
                }
            }
        }
    }
}