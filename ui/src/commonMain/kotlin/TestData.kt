import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.node.NodeType
import ui.model.Camera
import ui.model.UiEdge
import ui.model.UiNode
import ui.state.GraphEditorLayoutState
import java.util.Map

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

val itemAndRecipe = initItemAndRecipe()

fun initItemAndRecipe(): ItemAndRecipeState{
    val items = LinkedHashMap<Long, Item>().apply {
        this[0L] = Item("Iron Ore")
        this[1L] = Item("Iron Ingot")
        this[2L] = Item("Iron Plate")
        this[3L] = Item("Iron Screw")
        this[4L] = Item("Frame")
        this[5L] = Item("Scrap")
    }

    val recipes = LinkedHashMap<Long, Recipe>().apply {
        this[0L] = Recipe(
            "Iron Recycling",
            "Cons 1",
            Map.of<Item, Double>(items[5L], 2.0),
            Map.of<Item, Double>(items[0L], 1.0),
            items[0L]
        )
        this[1L] = Recipe(
            "Iron Smelting",
            "Cons 1",
            Map.of<Item, Double>(items[0L], 2.0),
            Map.of<Item, Double>(items[1L], 1.0),
            items[1L]
        )
        this[2L] = Recipe(
            "Iron Plates",
            "Cons 1",
            Map.of<Item, Double>(items[1L], 4.0),
            Map.of<Item, Double>(items[2L], 3.0),
            items[2L]
        )
        this[3L] = Recipe(
            "Iron Light Frames",
            "Cons 1",
            Map.of<Item, Double>(
                items[2L],
                6.0,
                items[3L],
                16.0
            ),
            Map.of<Item, Double>(
                items[4L],
                2.0,
                items[5L],
                4.0
            ),
            items[4L]
        )
    }

    return ItemAndRecipeState(items, recipes)
}


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