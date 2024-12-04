package com.example.quizpulse.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

/**
 * Screen to display a list of categories.
 * Clicking on a category card will navigate to the difficulty screen.
 *
 * @param navController The navigation controller to use for navigation
 * @param modifier The modifier to apply to the screen
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CategoriesScreen(
    navController: NavController,
    modifier: Modifier
) {
    // List of categories to display
    val categories = listOf(
        "Animals",
        "History",
        "Sports",
        "Geography",
        "General Knowledge"
    )

    var selectedCategory by remember { mutableStateOf<String?>(null) }

    // Layout for the category cards
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 200.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title or header text
        Text(
            text = "Select a Category",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display the categories as a list of cards
        LazyColumn {
            items(categories.size) { index ->
                CategoryCard(
                    categoryName = categories[index],
                    isSelected = categories[index] == selectedCategory,
                    onClick = { selectedCategory = categories[index] }
                )
            }
        }

        if (selectedCategory?.isNotEmpty() == true) {
            Button(
                onClick = {
                    // Pass the selected category to DifficultyScreen
                    navController.navigate("difficulty/${selectedCategory}")
                },
                enabled = selectedCategory != null,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .padding(top = 50.dp)
                    .width(200.dp)
                    .height(50.dp)
            ) {
                Text(text = "Continue", fontSize = 23.sp)
            }
        }

    }
}

/**
 * Composable function for displaying a category card.
 *
 * @param categoryName The name of the category
 * @param isSelected Whether the card is selected or not
 * @param onClick The callback to be invoked when the card is clicked
 * @return The category card composable function
 */
@Composable
fun CategoryCard(
    categoryName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Card representing a category
    Card(
        modifier = Modifier
            .width(350.dp)
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(3.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryName,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}