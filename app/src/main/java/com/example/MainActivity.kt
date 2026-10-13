package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.GameViewModel
import com.example.ui.MainApp
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp(viewModel = viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.progressState.value.musicEnabled) {
            viewModel.audioManager.startBackgroundMusic()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.audioManager.stopBackgroundMusic()
        viewModel.audioManager.stopSpeaking()
    }
}
