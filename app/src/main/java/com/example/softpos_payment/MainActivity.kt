package com.example.softpos_payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.example.softpos_payment.presentation.navigation.Navigation
import com.example.softpos_payment.ui.theme.SoftPOS_PaymentTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            SoftPOS_PaymentTheme {

                Navigation()
            }
        }
    }
}