package ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import org.example.factory.Item
import ui.logic.GraphEditorLogic
import ui.model.Camera
import ui.model.UiEdge
import ui.screen.EdgeDrawer
import ui.screen.ItemColumn
import ui.state.GraphEditorLayoutState


fun main() = application {
    var controller by remember { mutableStateOf(GraphEditorLogic()) }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Test App"
    ) {
        AppTheme {
            EdgeDrawer(
                controller,
                3f,
                Color.Black,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}