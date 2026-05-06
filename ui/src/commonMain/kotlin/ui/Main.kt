package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import ui.graph.GraphScreen
import ui.logic.GraphEditorLogic
import ui.screen.EdgeDrawer


fun main() {
    val controller = GraphEditorLogic()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test App"
        ) {
            AppTheme {
                GraphScreen(controller)
            }
        }
    }
}