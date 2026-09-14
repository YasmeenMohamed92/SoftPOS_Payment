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
                    errorMessage = null
                )
            }

            AmountEntryIntent.OnPaymentSuccessConsumed -> {
                _uiState.value = _uiState.value.copy(
                    paymentResult = null
                )
            }
        }
    }

    private fun onAmountChanged(amount: String) {

        val value = amount.toDoubleOrNull()

        // Negative numbers are invalid.
        // Zero is allowed to pass validation,
        // but will be handled in processPayment().
        val isValid =
            value != null && value >= 0.0

        _uiState.value = _uiState.value.copy(
            amount = amount,
            isAmountValid = isValid,
            paymentResult = null,
            errorMessage = null
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
         * Hard-coded business validation:
         * Zero amount is not allowed.
         */
        if (amount == 0.0) {

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                paymentResult = null,
                errorMessage = "Amount should be more than zero"
            )

            return
        }

        viewModelScope.launch(Dispatchers.IO) {

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                paymentResult = null,
                errorMessage = null
            )

            val result =
                processPaymentUseCase(amount)

            when (result) {

                is PaymentResult.Success -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        paymentResult = result,
                        errorMessage = null
                    )
                }

                is PaymentResult.Failure -> {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        paymentResult = null,
                        errorMessage = result.message
                    )
                }
            }
        }
    }
}