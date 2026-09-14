package com.example.softpos_payment.domain.model

sealed class PaymentResult {

    data class Success(
        val amount: Double,
        val transactionId: String,
        val status: String,
        val timestamp: String
    ) : PaymentResult()

    data class Failure(
        val message: String
    ) : PaymentResult()
}