package com.example.softpos_payment.domain.repository

import com.example.softpos_payment.domain.model.PaymentResult

interface PaymentRepository {

    suspend fun processPayment(
        amount: Double
    ): PaymentResult
}