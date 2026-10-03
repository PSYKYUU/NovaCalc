package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.AlexCalcViewModel
import com.example.ui.CalculatorScreen

class MainActivity : ComponentActivity() {

    private val viewModel: AlexCalcViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorScreen(viewModel = viewModel)
        }
    }
}
