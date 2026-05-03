package ui

import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.graph.GraphScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Test App"
    ) {
        GraphScreen()
    }
}