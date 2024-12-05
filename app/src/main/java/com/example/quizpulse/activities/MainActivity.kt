package com.example.quizpulse.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quizpulse.api.QuestionsManager
import com.example.quizpulse.db.AppDatabase
import com.example.quizpulse.destinations.Destination
import com.example.quizpulse.screens.CategoriesScreen
import com.example.quizpulse.screens.DifficultyScreen
import com.example.quizpulse.screens.QuizScreen
import com.example.quizpulse.screens.ResultScreen
import com.example.quizpulse.ui.theme.QuizpulseTheme
import com.example.quizpulse.viewmodel.ThemeViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * MainActivity class responsible for setting up the initial UI and navigation for the app
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizpulseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    // Create a NavHostController to handle navigation
                    val navController = rememberNavController()

                    // Create a database instance
                    val db = AppDatabase.getInstance(applicationContext)

                    // Create a QuestionsManager instance to handle questions and answers
                    val questionManager = QuestionsManager(db)

                    // initialize the theme viewmodel
                    val themeViewModel: ThemeViewModel = viewModel()

                    // firestore db
                    val fs_db = Firebase.firestore

                    App(navController = navController, modifier = Modifier.padding(innerPadding), questionManager, themeViewModel, fs_db)
                }
            }
        }
    }
}

/**
 * App composable function responsible for the main UI layout
 *
 * @param navController The NavController instance to handle navigation
 * @param modifier Additional modifier for the layout
 * @param questionManager The QuestionsManager instance to handle questions and answers
 * @param themeViewModel The ThemeViewModel instance to manage the theme mode
 * @param fs_db The Firebase
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(
    navController: NavController,
    modifier: Modifier = Modifier,
    questionManager: QuestionsManager,
    themeViewModel: ThemeViewModel,
    fs_db: FirebaseFirestore
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current // Get the context
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()

    QuizpulseTheme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                // Top bar
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        titleContentColor = MaterialTheme.colorScheme.onSecondary,
                    ),
                    title = {
                        Text(
                            "Quizpulse",
                            fontSize = 30.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    // arrow back
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    },
                    actions = {
                        // Menu Icon with Dropdown Menu
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            // Dark Mode Toggle
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = if(isDarkMode) "Dark" else "Light")
                                        Spacer(modifier = Modifier.weight(1f))
                                        Switch(
                                            checked = isDarkMode,
                                            onCheckedChange = {
                                                themeViewModel.toggleDarkMode()
                                            },
                                        )
                                    }
                                },
                                onClick = {
                                    themeViewModel.toggleDarkMode() // Toggle Dark Mode
                                    menuExpanded = false
                                }
                            )

                            // Top Scores
                            DropdownMenuItem(
                                text = { Text("Top Scores") },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, TopScoresActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }
                            )

                            // Help
                            DropdownMenuItem(
                                text = { Text("Help") },
                                onClick = {
                                    menuExpanded = false
                                    val intent = Intent(context, HowToPlayActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)
                                }
                            )

                            // Logout
                            DropdownMenuItem(
                                text = { Text("Log Out") },
                                onClick = {
                                    menuExpanded = false
                                    performSignOut(context)
                                }
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            content = {
                NavHost(
                    navController = navController as NavHostController,
                    startDestination = Destination.Category.route
                ) {
                    composable(Destination.Category.route) {
                        CategoriesScreen(navController, modifier)
                    }
                    composable(Destination.Difficulty.route) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        DifficultyScreen(navController, modifier, category)
                    }
                    composable(Destination.Quiz.route) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""
                        QuizScreen(navController, modifier, questionManager, category, difficulty, fs_db)
                    }
                    composable(Destination.Result.route) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category") ?: ""
                        val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
                        val difficulty = backStackEntry.arguments?.getString("difficulty") ?: ""
                        ResultScreen(navController, modifier, category, difficulty, score)
                    }
                }
            }
        )
    }
}

/**
 * Function to perform Firebase sign out and navigate to the SignIn activity
 *
 * @param context The Context to obtain the current activity
 * @return Nothing, but it will navigate to the SignIn activity and clear the backstack
 * if the user is signed out successfully.
 */
private fun performSignOut(context: Context) {
    val auth = FirebaseAuth.getInstance()

    auth.signOut() // Sign out from Firebase
    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()

    // Redirect to SignIn activity and clear backstack
    val intent = Intent(context, SignInActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}
