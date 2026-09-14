package com.example.softpos_payment.data.model

data class PaymentResponseDto(
    val transactionId: String? = null,
    val status: String,
    val timestamp: String? = null,
    val message: String? = null
)