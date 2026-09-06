package com.example.softpos_payment.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountEntryScreen(
    amount: String,
    onAmountChange: (String) -> Unit,
    onProceedClick: () -> Unit
) {

    val isValidAmount =
        amount.toDoubleOrNull()?.let {
            it > 0
        } == true

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Soft POS Payment" , Modifier.padding(horizontal = 100.dp) )
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Enter Payment Amount",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            TextField(
                value = amount,
                onValueChange = onAmountChange,
                label = {
                    Text("Payment Amount")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = onProceedClick,
                enabled = isValidAmount,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Proceed")
            }
        }
    }
}