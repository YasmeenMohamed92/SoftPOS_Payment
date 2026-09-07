package com.example.softpos_payment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.softpos_payment.screens.AmountEntryScreen
import com.example.softpos_payment.screens.PaymentConfirmation
import com.example.softpos_payment.viewmodel.PaymentViewModel

@Composable
fun Navigation(
    paymentViewModel: PaymentViewModel = viewModel()
) {

    val navController = rememberNavController()

    val amount by paymentViewModel.amount.collectAsState()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {

            AmountEntryScreen(

                amount = amount,

                onAmountChange = { newAmount ->
                    paymentViewModel.updateAmount(newAmount)
                },

                onProceedClick = {
                    navController.navigate("confirm")
                }
            )
        }

        composable("confirm") {

            PaymentConfirmation(

                amount = amount,

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}