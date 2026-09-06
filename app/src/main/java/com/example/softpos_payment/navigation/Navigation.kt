package com.example.softpos_payment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.softpos_payment.screens.AmountEntryScreen
import com.example.softpos_payment.screens.PaymentConfirmation

@Composable
fun Navigation() {

    val navController = rememberNavController()

    var amount by rememberSaveable {
        mutableStateOf("")
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            AmountEntryScreen(
                amount = amount,

                onAmountChange = {
                    amount = it
                },

                onProceedClick = {
                    navController.navigate("confirm/$amount")
                }
            )
        }

        composable("confirm/{amount}") { backStackEntry ->

            val amount =
                backStackEntry.arguments
                    ?.getString("amount")
                    .orEmpty()

            PaymentConfirmation(
                amount = amount,

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}