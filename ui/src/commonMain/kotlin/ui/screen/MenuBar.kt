package ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.PathParser
import org.example.data.ItemAndRecipeState
import org.example.graph.Graph
import save.SaveHandler
import ui.io.loadItemAndRecipe
import ui.io.selectAndReadJsonFile
import ui.logic.GraphEditorLogic
import ui.model.Camera
import ui.state.AppState
import ui.state.GraphEditorLayoutState

@Composable
fun MenuBar(
    appState: AppState,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }
    val controller by appState.controller.collectAsState()

    Row(
        modifier = modifier
    ) {
        Column {
            Text(
                text = "File",
                modifier = Modifier.clickable{ expanded = !expanded }
            )

            if(expanded){
                Text(
                    text = "New",
                    modifier = Modifier.clickable{ appState.createNewGraph() }
                )

                Text(
                    text = "Open",
                    modifier = Modifier.clickable{ appState.openGraph() }
                )

                Text(
                    text = "Save",
                    modifier = Modifier.clickable{ appState.saveGraph() }
                )

                Text(
                    text = "Save As",
                    modifier = Modifier.clickable{ appState.saveAsGraph() }
                )
            }
        }

        Text(
            text = "Item And Recipe",
            modifier = Modifier.clickable{
                val json = selectAndReadJsonFile()

                if(json == null) {
                    appState.generateError("File not selected.")
                    return@clickable
                }

                val itemAndRecipeState = loadItemAndRecipe(json)
                controller?.mergeItemAndRecipeState(itemAndRecipeState)
            }
        )
    }
}