package ru.alexeypostnov.chordflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import ru.alexeypostnov.chordflow.navigation.AuthorsListNavigationScreen
import ru.alexeypostnov.chordflow.ui.theme.ChordFlowTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChordFlowTheme{
                Navigator(
                    screen = AuthorsListNavigationScreen()
                ) { navigator ->
                    FadeTransition(navigator)
                }
            }
        }
    }
}