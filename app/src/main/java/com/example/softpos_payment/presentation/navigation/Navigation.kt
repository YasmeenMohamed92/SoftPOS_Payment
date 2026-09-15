package com.example.softpos_payment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.softpos_payment.domain.model.PaymentResult
import com.example.softpos_payment.presentation.amount.AmountEntryScreen
import com.example.softpos_payment.presentation.confirmation.PaymentConfirmationScreen

object Routes {

    const val AMOUNT = "amount"
    const val CONFIRMATION = "confirmation"
}

@Composable
fun Navigation() {
    val navController: NavHostController =
    rememberNavController()

    var paymentResult by remember {
        mutableStateOf<PaymentResult.Success?>(null)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.AMOUNT
    ) {

        composable(Routes.AMOUNT) {

            AmountEntryScreen(

                onPaymentSuccess = { result ->

                    paymentResult = result

                    navController.navigate(
                        Routes.CONFIRMATION
                    )
                }
            )
        }

        composable(Routes.CONFIRMATION) {

            paymentResult?.let { result ->

                PaymentConfirmationScreen(

                    paymentResult = result,

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

