package ui.logic

import androidx.compose.ui.geometry.Offset
import initGraph
import itemAndRecipe
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.node.MergerNode
import org.example.graph.node.NodeType
import org.example.graph.node.SinkNode
import org.example.graph.node.SourceNode
import org.example.graph.node.SplitterNode
import org.example.graph.node.TransformationNode
import testState
import ui.model.FilterOption
import ui.model.toUiNode
import util.linkedHashMapOf
import kotlin.math.pow

class GraphEditorLogic {
    private var _state = MutableStateFlow(testState)
    val state = _state.asStateFlow()

    private val itemAndRecipeState = itemAndRecipe

    fun updateZoom(scrollDelta: Float, cursor: Offset){
        _state.update { state ->
            val camera = state.camera
            val oldZoom = camera.zoom

            // Calculate new zoom level
            val newZoom = (oldZoom * 1.05f.pow(-scrollDelta))
                .coerceIn(camera.zoomMin, camera.zoomMax)

            // Find the world position under the cursor before zooming
            // WorldPos = (ScreenPos / Zoom) + Offset
            val worldUnderCursor = (cursor / oldZoom) + camera.offset

            // We want the new world position under the cursor to match the old one
            // ScreenPos / NewZoom + NewOffset = WorldUnderCursor
            // So: NewOffset = WorldUnderCursor - (ScreenPos / NewZoom)
            val newOffset = worldUnderCursor - (cursor / newZoom)

            state.copy(
                camera = camera.copy(
                    zoom = newZoom,
                    offset = newOffset
                )
            )
        }
    }

    fun updateCameraPosition(dragAmount: Offset){
        _state.update{ state ->
            val camera = state.camera
            state.copy(
                camera = camera.copy(
                    offset = camera.offset - (dragAmount / camera.zoom)
                )
            )
        }
    }

    fun updateNodePosition(nodeId: Long, delta: Offset){
        state.value.nodes

        _state.update { state ->
            val node = state.nodes[nodeId] ?: return@update state

            state.nodes[nodeId] = node.copy( position = node.position + delta )

            state
        }
    }

    private val _isRecipeMenuDisplayed = MutableStateFlow(false)
    val isRecipeMenuDisplayed = _isRecipeMenuDisplayed.asStateFlow()

    private val _recipeMenuOffset = MutableStateFlow(Offset(0f, 0f))
    val recipeMenuOffset = _recipeMenuOffset.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _filterOption = MutableStateFlow<FilterOption?>(null)
    val filterOption = _filterOption.asStateFlow()

    fun updateRecipeMenuOffset(offset: Offset){
        _recipeMenuOffset.update { it + offset }
    }

    fun setRecipeMenuDisplayed(displayed: Boolean){
        _isRecipeMenuDisplayed.update { displayed }
    }

    fun updateSearchText(newSearchText: String){
        _searchText.update { newSearchText }
    }

    fun updateFilterOption(filterOption: FilterOption?){
        _filterOption.update { filterOption }
    }

    @OptIn(FlowPreview::class)
    val filteredItems = searchText
        .debounce(500)
        .combine(filterOption){ searchText, filterOption ->
            if(searchText.isBlank() || filterOption == null) emptyList<Recipe>()
            else getListOfRecipe(searchText, filterOption)
        }

    fun getListOfRecipe(search: String, filterOption: FilterOption): List<Recipe> {
        return when(filterOption){
            FilterOption.RECIPE -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.name.lowercase().contains(search.lowercase())
                }

            FilterOption.INPUT_MATERIAL -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.inputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }

            FilterOption.OUTPUT_MATERIAL -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.outputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }
        }
        .values
        .toList()
    }

    private val graph = initGraph(itemAndRecipe)

    fun getNodeName(nodeId: Long): String{
        return when(val node = graph.getNode(nodeId)){
            is TransformationNode -> node.recipe.name
            is SplitterNode -> "Splitter"
            is MergerNode -> "Merger"
            is SourceNode -> "Source"
            is SinkNode -> "Sink"
            else -> "No Name"
        }
    }

    fun getInputMaterials(nodeId: Long): LinkedHashMap<Item, Double?> {
        return when(val node = graph.getNode(nodeId)){
            is TransformationNode -> node.recipe.inputMaterials
            is SourceNode -> linkedHashMapOf<Item, Double?>(node.item, null)
            is MergerNode -> linkedHashMapOf<Item, Double?>(node.item, null)
            else -> emptyMap
        }
    }

    fun getOutputMaterial(nodeId: Long): LinkedHashMap<Item, Double?> {
        return when(val node = graph.getNode(nodeId)){
            is TransformationNode -> node.recipe.outputMaterials
            is SourceNode -> linkedHashMapOf<Item, Double?>(node.item, null)
            is MergerNode -> linkedHashMapOf<Item, Double?>(node.item, null)
            else -> emptyMap
        }
    }

    fun addNode(recipe: Recipe, type: NodeType, offset: Offset){
        _state.update { state ->
            val node = TransformationNode(recipe)
            graph.addNode(node)

            state.nodes[node.id] = node.toUiNode(offset)

            state
        }
    }

    companion object {
        val emptyMap = LinkedHashMap<Item, Double?>()
    }
}

