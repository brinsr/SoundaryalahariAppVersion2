package com.example.soundaryalahariappversion2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.soundaryalahariappversion2.ui.home.HomeDestination
import com.example.soundaryalahariappversion2.ui.home.HomeScreen
import com.example.soundaryalahariappversion2.ui.home.SearchViewModel
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaDetailsDestination
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaDetailsScreen
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEditDestination
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEditScreen
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryDestination
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryScreen

@Composable
fun SoundaryalahariNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,modifier = modifier
    ) {
        //Home
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToShlokaEntry = { navController.navigate(ShlokaEntryDestination.route) },
                navigateToShlokaDetails = {
                    navController.navigate("${ShlokaDetailsDestination.route}/${it}")
                },
                searchViewModel = SearchViewModel()
            )
        }
        // Shloka Entry
        composable(route = ShlokaEntryDestination.route) {
            ShlokaEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        //Shloka Details
        composable(
            route = ShlokaDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(ShlokaDetailsDestination.shlokaNumberArg){
                type = NavType.IntType
                }
            )
        ){
            ShlokaDetailsScreen(
                navigateToEditShloka = { navController.navigate("${ShlokaEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() })
        }

        //Shloka Edit
        composable(
            route = ShlokaEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ShlokaEditDestination.shlokaNumArg) {
                type = NavType.IntType
            })
        ){
            ShlokaEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() },
            )
        }
    }
}