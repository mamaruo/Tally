package com.mamaruo.tally.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mamaruo.tally.ui.category.CategoryEditorScreen
import com.mamaruo.tally.ui.category.CategoryManageScreen
import com.mamaruo.tally.ui.home.HomeScreen
import com.mamaruo.tally.ui.transaction.TransactionEditorScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onAddTransaction = {
                    navController.navigate(Screen.AddTransaction.route)
                },
                onEditTransaction = { transactionId ->
                    navController.navigate(Screen.EditTransaction.createRoute(transactionId))
                },
                onManageCategories = {
                    navController.navigate(Screen.CategoryManage.route)
                }
            )
        }

        composable(Screen.AddTransaction.route) {
            TransactionEditorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") { type = NavType.LongType }
            )
        ) {
            TransactionEditorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CategoryManage.route) {
            CategoryManageScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAddCategory = {
                    navController.navigate(Screen.AddCategory.route)
                }
            )
        }

        composable(Screen.AddCategory.route) {
            CategoryEditorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
