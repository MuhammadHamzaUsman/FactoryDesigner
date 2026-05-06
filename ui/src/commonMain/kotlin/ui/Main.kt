package ui

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import inputMaterial
import ironIngot
import nameFromId
import org.example.factory.Machine
import org.example.factory.Recipe
import outputMaterial
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
                MachineSelectionMenu(
                    Machine(
                        LinkedHashSet<Recipe>().apply {
                            Recipe(
                                "Something",
                                nameFromId(0L),
                                inputMaterial(0L),
                                outputMaterial(0L),
                                ironIngot
                            )
                        }
                    )
                )
//                GraphScreen(controller)
            }
        }
    }
}