package com.example.softpos_payment.presentation.amount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.softpos_payment.domain.model.PaymentResult

@Composable
fun AmountEntryScreen(
    onPaymentSuccess: (PaymentResult.Success) -> Unit,
    viewModel: AmountEntryViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    /*
     * Navigate when payment becomes successful.
     */
    LaunchedEffect(uiState.paymentState) {

        val paymentState = uiState.paymentState

        if (paymentState is PaymentState.Success) {

            onPaymentSuccess(
                paymentState.paymentResult
            )

            viewModel.onIntent(
                AmountEntryIntent.OnPaymentSuccessConsumed
            )
        }
    }

    /*
     * Get error message if current state is Error.
     */
    val errorMessage =
        (uiState.paymentState as? PaymentState.Error)?.message

    /*
     * Check if payment is currently loading.
     */
    val isLoading =
        uiState.paymentState is PaymentState.Loading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "SoftPOS Payment",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(

            value = uiState.amount,

            onValueChange = { newValue ->

                val isValidInput =
                    newValue.all { char ->
                        char.isDigit() || char == '.'
                    }

                val hasOnlyOneDot =
                    newValue.count { it == '.' } <= 1

                if (
                    isValidInput &&
                    hasOnlyOneDot
                ) {

                    viewModel.onIntent(
                        AmountEntryIntent.OnAmountChanged(
                            newValue
                        )
                    )
                }
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Amount")
            },

            placeholder = {
                Text("Enter amount")
            },

            singleLine = true,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),

            isError = errorMessage != null
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(

            onClick = {
                viewModel.onIntent(
                    AmountEntryIntent.OnProcessPaymentClicked
                )
            },

            enabled =
                uiState.isAmountValid &&
                        !isLoading,

            modifier = Modifier.fillMaxWidth()
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .height(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )

            } else {

                Text("Proceed")
            }
        }
    }

    /*
     * Error state
     */
    if (uiState.paymentState is PaymentState.Error) {

        AlertDialog(

            onDismissRequest = {
                viewModel.onIntent(
                    AmountEntryIntent.OnErrorDismissed
                )
            },

            title = {
                Text("Payment Failed")
            },

            text = {
                Text(
                    text = errorMessage
                        ?: "Something went wrong."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        viewModel.onIntent(
                            AmountEntryIntent.OnErrorDismissed
                        )
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
}

