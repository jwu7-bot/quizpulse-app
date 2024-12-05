package com.example.quizpulse.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizpulse.activities.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Screen for displaying top scores
 *
 * @param context Context for accessing resources
 * @param modifier Modifier for the screen
 */
@Composable
fun TopScoresScreen(
    context: Context,
    modifier: Modifier,
) {
    val db = FirebaseFirestore.getInstance()
    val scores = remember { mutableStateOf<Map<String, Map<String, List<ScoreEntry>>>>(emptyMap()) }

    // Fetching scores from Firestore
    LaunchedEffect(Unit) {
        db.collection("userScore")
            .get()
            .addOnSuccessListener { result ->
                val groupedScores = result.documents
                    .mapNotNull { doc ->
                        val category = doc.getString("category") ?: return@mapNotNull null
                        val difficulty = doc.getString("difficulty") ?: return@mapNotNull null
                        val score = doc.getLong("score")?.toInt() ?: return@mapNotNull null
                        ScoreEntry(
                            category = category,
                            difficulty = difficulty,
                            score = score,
                            userEmail = doc.getString("userEmail") ?: "Unknown",
                            date = doc.getString("date") ?: "Unknown"
                        )
                    }
                    .groupBy { it.category }
                    .mapValues { (_, entries) ->
                        entries.groupBy { it.difficulty }
                            .mapValues { (_, difficultyEntries) ->
                                difficultyEntries.sortedByDescending { it.score }.take(3)
                            }
                    }

                scores.value = groupedScores
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Failed to fetch scores", e)
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 20.dp)
    ) {
        // LazyColumn for the scores
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Make space for the Play button
        ) {
            scores.value.forEach { (category, difficulties) ->
                item {
                    // Category Title
                    Text(
                        text = category,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    difficulties.forEach { (difficulty, entries) ->
                        // Difficulty Header
                        Text(
                            text = "Difficulty: $difficulty",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 1.dp),
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        // Rank Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "User",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "Score",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // Rank Table Rows
                        entries.forEachIndexed { index, entry ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline)),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = entry.userEmail.split("@")[0],
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    text = entry.score.toString(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }

        // Play button
        Button(
            onClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp)
                .width(200.dp)
                .height(50.dp)
        ) {
            Text("Play", fontSize = 23.sp)
        }
    }
}

/**
 * Data class entries in Firestore
 *
 * @property category Category of the quiz
 * @property difficulty Difficulty level of the quiz
 * @property score Score achieved by the user
 * @property userEmail Email of the user
 */
data class ScoreEntry(
    val category: String,
    val difficulty: String,
    val score: Int,
    val userEmail: String,
    val date: String
)