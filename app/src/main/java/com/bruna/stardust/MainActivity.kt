package com.bruna.stardust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bruna.stardust.ui.StardustApp
import com.bruna.stardust.ui.theme.StardustTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StardustTheme {
                StardustApp()
            }
        }
    }
}