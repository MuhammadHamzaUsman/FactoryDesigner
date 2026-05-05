package ui

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import org.example.factory.Item
import ui.model.Camera
import ui.model.UiEdge
import ui.screen.ItemColumn
import ui.state.GraphEditorLayoutState

val layout = GraphEditorLayoutState(
    nodes = mutableMapOf(),
    edges = mutableMapOf(
        0L to UiEdge(0L, listOf(
                Offset(1f, 1f),
                Offset(1f, 10f),
                Offset(20f, 10f),
                Offset(20f, 30f),
                Offset(40f, 30f),
                Offset(40f, 40f)
            )
        )
    ),
    camera = Camera(Offset.Zero, mutableFloatStateOf(1f).value)
)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Test App"
    ) {
        AppTheme {
            ItemColumn(
                LinkedHashMap<Item, Double?>().apply {
                    this[Item("Iron")] = 10.0
                    this[Item("Scrap")] = 30.0
                    this[Item("Water")] = 50.0
                    this[Item("Heat")] = 19.0
                },
                onValueChange = {}
            )
        }
    }
}