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

    LaunchedEffect(uiState.paymentResult) {

        val result = uiState.paymentResult

        if (result is PaymentResult.Success) {

            onPaymentSuccess(result)

            viewModel.onIntent(
                AmountEntryIntent.OnPaymentSuccessConsumed
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

            isError = uiState.errorMessage != null
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
                        !uiState.isLoading,

            modifier = Modifier.fillMaxWidth()
        ) {

            if (uiState.isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )

            } else {

                Text("Proceed")
            }
        }

        if (uiState.errorMessage != null) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    if (uiState.errorMessage != null) {

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
                Text(uiState.errorMessage!!)
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