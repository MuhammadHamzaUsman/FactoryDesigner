package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import ui.graph.GraphScreen
import ui.state.AppState


fun main() {

    val appState = AppState(null)

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test App"
        ) {
            AppTheme {
                GraphScreen(
                    appState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}