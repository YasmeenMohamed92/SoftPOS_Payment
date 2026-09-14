package com.example.softpos_payment.data.repository

import com.example.softpos_payment.data.model.PaymentRequestDto
import com.example.softpos_payment.data.remote.PaymentApi
import com.example.softpos_payment.domain.model.PaymentResult
import com.example.softpos_payment.domain.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class PaymentRepositoryImpl @Inject constructor(
    private val paymentApi: PaymentApi
) : PaymentRepository {

    override suspend fun processPayment(
        amount: Double
    ): PaymentResult {

        return withContext(Dispatchers.IO) {

            try {

                val response = paymentApi.processPayment(
                    PaymentRequestDto(amount)
                )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body == null) {

                        PaymentResult.Failure(
                            "Empty response from server."
                        )

                    } else if (
                        body.status.equals(
                            "APPROVED",
                            ignoreCase = true
                        )
                    ) {

                        PaymentResult.Success(
                            amount = amount,
                            transactionId = body.transactionId.orEmpty(),
                            status = body.status,
                            timestamp = body.timestamp.orEmpty()
                        )

                    } else {

                        /*
                         * HTTP 200 but payment was not approved.
                         * Use the message returned by the API.
                         */
                        PaymentResult.Failure(
                            body.message.orEmpty()
                        )
                    }

                } else {

                    /*
                     * HTTP error.
                     * Try to extract the message returned by the API.
                     */
                    val errorBody =
                        response.errorBody()?.string()

                    val apiMessage =
                        extractApiMessage(errorBody)

                    PaymentResult.Failure(
                        apiMessage
                    )
                }

            } catch (e: IOException) {

                PaymentResult.Failure(
                    e.message ?: "Network error"
                )

            } catch (e: Exception) {

                PaymentResult.Failure(
                    e.message ?: "Unknown error"
                )
            }
        }
    }

    private fun extractApiMessage(
        errorBody: String?
    ): String {

        if (errorBody.isNullOrBlank()) {
            return "Unknown server error"
        }

        return try {

            val json = JSONObject(errorBody)

            json.optString(
                "message",
                "Unknown server error"
            )

        } catch (e: Exception) {

            errorBody
        }
    }
}