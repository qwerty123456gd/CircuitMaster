package com.example.circuitmaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.example.circuitmaster.ui.screen.MainContainerScreen
import com.example.circuitmaster.ui.screen.WelcomeScreen
import com.example.circuitmaster.ui.theme.CircuitMasterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CircuitMasterTheme {
                var isStarted by remember { mutableStateOf(false) }

                if (!isStarted) {
                    WelcomeScreen(onStartClicked = { isStarted = true })
                } else {
                    MainContainerScreen()
                }
            }
        }
    }
}