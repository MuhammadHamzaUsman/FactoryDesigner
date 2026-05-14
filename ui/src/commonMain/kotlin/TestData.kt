import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe
import ui.model.Camera
import ui.state.GraphEditorLayoutState

val testState = GraphEditorLayoutState(
    nodes = mutableStateMapOf(),
    edges = mutableStateMapOf(),
    camera = Camera(Offset.Zero, 1f),
    null,
    mutableStateMapOf()
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
            LinkedHashMap<Item, Double>().apply {this[items[5L]!!] = 2.0},
            LinkedHashMap<Item, Double>().apply {this[items[0L]!!] = 1.0},
            items[0L]
        )
        this[1L] = Recipe(
            "Iron Smelting",
            "Cons 1",
            LinkedHashMap<Item, Double>().apply {this[items[0L]!!] = 2.0},
            LinkedHashMap<Item, Double>().apply {this[items[1L]!!] = 1.0},
            items[1L]
        )
        this[2L] = Recipe(
            "Iron Plates",
            "Cons 1",
            LinkedHashMap<Item, Double>().apply {this[items[1L]!!] = 4.0},
            LinkedHashMap<Item, Double>().apply {this[items[2L]!!] = 3.0},
            items[2L]
        )
        this[3L] = Recipe(
            "Iron Light Frames",
            "Cons 1",
            LinkedHashMap<Item, Double>().apply {
                this[items[2L]!!] = 6.0
                this[items[3L]!!] = 16.0
            },
            LinkedHashMap<Item, Double>().apply {
                this[items[4L]!!] = 2.0
                this[items[5L]!!] = 4.0
            },
            items[4L]
        )
    }

    return ItemAndRecipeState(items, recipes)
}