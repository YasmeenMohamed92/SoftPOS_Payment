package com.example.softpos_payment.data.remote

import com.example.softpos_payment.data.model.PaymentRequestDto
import com.example.softpos_payment.data.model.PaymentResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {

    @POST("api/v1/payment/process")
    suspend fun processPayment(
        @Body request: PaymentRequestDto
    ): Response<PaymentResponseDto>
}