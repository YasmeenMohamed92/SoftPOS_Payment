package com.example.softpos_payment.presentation.amount

import com.example.softpos_payment.domain.model.PaymentResult

data class AmountEntryUiState(
    val amount: String = "",
    val isAmountValid: Boolean = false,
    val paymentState: PaymentState = PaymentState.Idle
)

sealed interface PaymentState {

    data object Idle : PaymentState

    data object Loading : PaymentState

    data class Success(
        val paymentResult: PaymentResult.Success
    ) : PaymentState

    data class Error(
        val message: String
    ) : PaymentState
}