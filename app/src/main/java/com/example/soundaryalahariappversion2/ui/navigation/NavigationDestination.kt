package com.example.soundaryalahariappversion2.ui.navigation

interface NavigationDestination {
    /**
     * Unique name to define path for a composable
     */
    val route: String
    /**
     * String reosurce id to that contains title to be displayed for the screen
     */
    val titleRes: Int
}