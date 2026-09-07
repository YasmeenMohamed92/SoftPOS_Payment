package com.example.softpos_payment.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PaymentViewModel : ViewModel() {

    private val _amount = MutableStateFlow("")

    val amount: StateFlow<String> = _amount.asStateFlow()

    fun updateAmount(value: String) {
        _amount.value = value
    }
}