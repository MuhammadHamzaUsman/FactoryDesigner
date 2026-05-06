import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import org.example.factory.Item
import org.example.graph.node.NodeType
import ui.model.Camera
import ui.model.UiEdge
import ui.model.UiNode
import ui.state.GraphEditorLayoutState

val testState = GraphEditorLayoutState(
    nodes = mutableMapOf(
        0L to UiNode(
            id = 0,
            position = Offset(10f, 10f),
            type = NodeType.TRANSFORMATION
        ),

        1L to UiNode(
            id = 1,
            position = Offset(50f, 50f),
            type = NodeType.TRANSFORMATION
        )

    ),
    edges = mutableStateMapOf(
        0L to UiEdge(0L, listOf(
            Offset(1f, 1f),
            Offset(1f, 10f),
            Offset(20f, 10f),
            Offset(20f, 30f),
            Offset(40f, 30f),
            Offset(40f, 40f)
        ))
    ),
    camera = Camera(Offset.Zero, 1f)
)

fun nameFromId(id: Long) = when(id){
    0L -> "Constructor"
    1L -> "Refinery"
    else -> "No"
}

fun machineCount(id: Long) = when(id){
    0L -> 10
    1L -> 15
    else -> "No"
}

fun inputMaterial(id: Long) = when(id){
    0L -> LinkedHashMap<Item, Double?>().apply {
        this[ironOre] = 3.0
    }
    1L -> LinkedHashMap<Item, Double?>().apply {
        this[ironIngot] = 4.0
    }
    else -> LinkedHashMap<Item, Double?>()
}

fun outputMaterial(id: Long) = when(id){
    0L -> LinkedHashMap<Item, Double?>().apply {
        this[ironIngot] = 1.0
    }
    1L -> LinkedHashMap<Item, Double?>().apply {
        this[ironPlate] = 3.0
    }
    else -> LinkedHashMap<Item, Double?>()
}

val ironOre = Item("Iron Ore")
val ironIngot = Item("Iron Ingot")
val ironPlate = Item("Iron Plate")