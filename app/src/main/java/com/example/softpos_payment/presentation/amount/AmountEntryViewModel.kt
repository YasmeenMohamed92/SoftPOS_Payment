package com.example.softpos_payment.presentation.amount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softpos_payment.domain.model.PaymentResult
import com.example.softpos_payment.domain.usecase.ProcessPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AmountEntryViewModel @Inject constructor(
    private val processPaymentUseCase: ProcessPaymentUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AmountEntryUiState())

    val uiState: StateFlow<AmountEntryUiState> =
        _uiState.asStateFlow()

    fun onIntent(intent: AmountEntryIntent) {

        when (intent) {

            is AmountEntryIntent.OnAmountChanged -> {
                onAmountChanged(intent.amount)
            }

            AmountEntryIntent.OnProcessPaymentClicked -> {
                processPayment()
            }

            AmountEntryIntent.OnErrorDismissed -> {
                _uiState.value = _uiState.value.copy(
                    paymentState = PaymentState.Idle
                )
            }

            AmountEntryIntent.OnPaymentSuccessConsumed -> {
                _uiState.value = _uiState.value.copy(
                    paymentState = PaymentState.Idle
                )
            }
        }
    }

    private fun onAmountChanged(amount: String) {

        val value = amount.toDoubleOrNull()

        val isValid =
            value != null && value >= 0.0

        _uiState.value = _uiState.value.copy(
            amount = amount,
            isAmountValid = isValid,
            paymentState = PaymentState.Idle
        )
    }

    private fun processPayment() {

        val currentState = _uiState.value

        if (!currentState.isAmountValid) {
            return
        }

        val amount =
            currentState.amount.toDoubleOrNull()
                ?: return

        /*
         * Business validation:
         * Zero amount is not allowed.
         */
        if (amount == 0.0) {

            _uiState.value = _uiState.value.copy(
                paymentState = PaymentState.Error(
                    message = "Amount should be more than zero"
                )
            )

            return
        }

        viewModelScope.launch(Dispatchers.IO) {


            _uiState.value = _uiState.value.copy(
                paymentState = PaymentState.Loading
            )

            try {

                val result =
                    processPaymentUseCase(amount)

                when (result) {

                    is PaymentResult.Success -> {

                        _uiState.value = _uiState.value.copy(
                            paymentState = PaymentState.Success(
                                paymentResult = result
                            )
                        )
                    }

                    is PaymentResult.Failure -> {

                        _uiState.value = _uiState.value.copy(
                            paymentState = PaymentState.Error(
                                message = result.message
                            )
                        )
                    }
                }

            } catch (e: Exception) {

                _uiState.value = _uiState.value.copy(
                    paymentState = PaymentState.Error(
                        message = e.message
                            ?: "Something went wrong. Please try again."
                    )
                )
            }
        }
    }
}

