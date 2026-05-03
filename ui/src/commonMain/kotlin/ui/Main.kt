package ui

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.compose.AppTheme
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.node.NodeType
import org.example.graph.node.TransformationNode
import ui.model.Camera
import ui.model.UiEdge
import ui.model.UiNode
import ui.screen.NodeCard
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
val iron = Item("Iron")
val scrap = Item("Scrap")

fun main() = application {
    AppTheme {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Test App"
        ) {

            NodeCard(
                uiNode = UiNode(
                    id = 0,
                    position = Offset(50f, 50f),
                    type = NodeType.TRANSFORMATION
                ),
                node = TransformationNode(
                    Recipe(
                        "Iron Waster",
                        "Constructor",
                        mutableMapOf(iron to 1.0),
                        mutableMapOf(scrap to 4.0),
                        scrap
                    )
                ),
            )
        }
    }
}