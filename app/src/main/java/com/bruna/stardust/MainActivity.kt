package com.bruna.stardust

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bruna.stardust.data.remote.ApodClient
import com.bruna.stardust.ui.theme.StardustTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StardustTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TodayScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!",
        )
        Text(
            text = "Teste para saber o que acontece",
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StardustTheme {
        Greeting("Stardust")
    }
}

@Composable
fun TodayScreen(modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("Carregando...") }

    LaunchedEffect(Unit) {
        title = try {
            ApodClient.api.getApod(perPage = 1).first().title
        } catch (e: Exception) {
            "Erro: ${e.message}"
        }
    }

    Text(text = title, modifier = modifier)
}