package com.example.quizpulse.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 *  ViewModel class responsible for managing the dark mode state in the UI
 */
class ThemeViewModel : ViewModel() {
    /**
     * MutableStateFlow to store the dark mode state, initially set to false (light mode)
     */
    private val _isDarkMode = MutableStateFlow(false)

    /**
     * StateFlow that emits the current dark mode state
     */
    val isDarkMode: StateFlow<Boolean> = _isDarkMode

    /**
     * Function to toggle the dark mode state
     */
    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }
}