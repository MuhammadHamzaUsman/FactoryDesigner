package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import ui.graph.GraphScreen
import ui.logic.GraphEditorLogic
import ui.screen.MachineSelectionMenu


fun main() {
    val controller = GraphEditorLogic()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test App"
        ) {
            AppTheme {
//                MachineSelectionMenu(controller)
                GraphScreen(controller)
            }
        }
    }
}