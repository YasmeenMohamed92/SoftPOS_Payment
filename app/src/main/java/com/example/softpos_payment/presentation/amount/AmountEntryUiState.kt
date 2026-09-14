package com.example.softpos_payment.presentation.amount

import com.example.softpos_payment.domain.model.PaymentResult

data class AmountEntryUiState(

    val amount: String = "",

    val isAmountValid: Boolean = false,

    val isLoading: Boolean = false,

    val paymentResult: PaymentResult? = null,

    val errorMessage: String? = null
)