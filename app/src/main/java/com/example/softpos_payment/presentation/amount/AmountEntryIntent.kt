package com.example.softpos_payment.presentation.amount

sealed class AmountEntryIntent {

    data class OnAmountChanged(
        val amount: String
    ) : AmountEntryIntent()

    data object OnProcessPaymentClicked : AmountEntryIntent()

    data object OnErrorDismissed : AmountEntryIntent()

    data object OnPaymentSuccessConsumed : AmountEntryIntent()
}