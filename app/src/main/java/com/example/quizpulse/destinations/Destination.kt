package com.example.quizpulse.destinations

/**
 * Sealed class to define and manage the routes for navigation within the application.
 *
 * Each object represents a specific screen or destination in the app,
 * with an associated route string that can include parameters for dynamic navigation.
 *
 * @param route The route string used to navigate to the destination.
 */
sealed class Destination(val route: String) {
    /**
     * Represents the category selection screen.
     * Route: "category"
     */
    object Category: Destination("category")

    /**
     * Represents the difficulty selection screen for a given category.
     * Route: "difficulty/{category}"
     */
    object Difficulty: Destination("difficulty/{category}")

    /**
     * Represents the quiz screen for a given category and difficulty.
     * Route: "quiz/{category}/{difficulty}"
     */
    object Quiz :Destination("quiz/{category}/{difficulty}")

    /**
     * Represents the result screen after a quiz completion.
     * Route: "resultScreen/{score}"
     */
    object Result : Destination("resultScreen/{category}/{difficulty}/{score}")
}