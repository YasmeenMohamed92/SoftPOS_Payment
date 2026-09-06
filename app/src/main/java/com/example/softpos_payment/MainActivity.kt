package com.example.softpos_payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.softpos_payment.navigation.Navigation
import com.example.softpos_payment.ui.theme.SoftPOS_PaymentTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SoftPOS_PaymentTheme {
                Navigation()
            }
        }
    }
}


