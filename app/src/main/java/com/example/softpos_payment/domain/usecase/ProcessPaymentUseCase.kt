package com.example.softpos_payment.domain.usecase

import com.example.softpos_payment.domain.model.PaymentResult
import com.example.softpos_payment.domain.repository.PaymentRepository
import javax.inject.Inject

class ProcessPaymentUseCase @Inject constructor(
    private val repository: PaymentRepository
) {

    suspend operator fun invoke(
        amount: Double
    ): PaymentResult {

        return repository.processPayment(amount)
    }
}