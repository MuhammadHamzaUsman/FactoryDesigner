package ui.logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import initGraph
import itemAndRecipe
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Edge
import org.example.graph.node.*
import testState
import ui.model.FilterOption
import ui.model.toUiEdge
import ui.model.toUiNode
import ui.state.GraphMode
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

            graph.forEachInputEdge(nodeId){
                state.edges[it.id]?.updateLastOffset(delta)
            }

            graph.forEachOutputEdge(nodeId){
                state.edges[it.id]?.updateFirstOffset(delta)
            }

            _state.update { currentState ->
                currentState.copy(edges = currentState.edges)
            }

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
            is SourceNode -> linkedHashMapOf(node.item, null)
            is MergerNode -> linkedHashMapOf(node.item, null)
            else -> emptyMap
        }
    }

    fun getOutputMaterial(nodeId: Long): LinkedHashMap<Item, Double?> {
        return when(val node = graph.getNode(nodeId)){
            is TransformationNode -> node.recipe.outputMaterials
            is SourceNode -> linkedHashMapOf(node.item, null)
            is MergerNode -> linkedHashMapOf(node.item, null)
            else -> emptyMap
        }
    }

    fun addNode(recipe: Recipe, offset: Offset){
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

    private val _graphMode = MutableStateFlow(GraphMode.NORMAL)
    val graphMode = _graphMode.asStateFlow()

    fun handleInputConnectorClick(nodeId: Long, item: Item, offset: Offset){
        val mode = graphMode.value

        if(mode == GraphMode.NORMAL){
            tempEdge = Edge(null, item, graph.getNode(nodeId))
            startEdgeDrawing(offset)
        }
        else if (mode == GraphMode.EDGE_DRAWING) {
            if(tempEdge?.source == null || item != tempEdge?.item){
                resetToNormal()
                return
            }

            tempEdge!!.destination = graph.getNode(nodeId)
            endEdgeDrawing(offset, false)
        }
    }

    fun handleOutputConnectorClick(nodeId: Long, item: Item, offset: Offset){
        val mode = graphMode.value

        if (mode == GraphMode.NORMAL) {
            tempEdge = Edge(graph.getNode(nodeId), item, null)
            startEdgeDrawing(offset)
        }
        else if(mode == GraphMode.EDGE_DRAWING){
            if(tempEdge?.destination == null || item != tempEdge?.item){
                resetToNormal()
                return
            }

            tempEdge!!.source = graph.getNode(nodeId)
            endEdgeDrawing(offset, true)
        }
    }

    private fun startEdgeDrawing(offset: Offset){
        pointsList.add(offset)
        _graphMode.update { GraphMode.EDGE_DRAWING }
        _isRecipeMenuDisplayed.update { false }
    }

    // asReversed is taken as parameter because when edge drawing is started from input connector then it is first
    // but when I update edge offset position I assume for output it is first offset and input last so by reversing
    // I maintain that assumption
    fun endEdgeDrawing(offset: Offset, asReversed: Boolean){
        pointsList.add(offset)
        val uiEdge = tempEdge!!.toUiEdge(if(asReversed) pointsList.asReversed() else pointsList)

        _state.update { currentState ->
            currentState.edges[uiEdge.id] = uiEdge
            currentState.copy(edges = currentState.edges)
        }

        graph.addEdge(tempEdge)
        resetToNormal()
    }

    private fun resetToNormal(){
        tempEdge = null
        pointsList.clear()
        _graphMode.update { GraphMode.NORMAL }
    }

    fun addPointToEdge(offset: Offset){
        pointsList.add(offset)
    }

    val pointsList = SnapshotStateList<Offset>()
    private var tempEdge: Edge? = null



    private var _isEdgeListDisplayed = MutableStateFlow(false)
    val isEdgeListDisplayed = _isEdgeListDisplayed.asStateFlow()

    private val _edgeListOffset = MutableStateFlow(Offset(0f, 0f))
    val edgeListOffset = _edgeListOffset.asStateFlow()

    private val _nodeId = MutableStateFlow<Long?>(null)
    val nodeId = _nodeId.asStateFlow()

    private val _item = MutableStateFlow<Item?>(null)
    val item = _item.asStateFlow()

    val edgesList = SnapshotStateList<Edge>()

    fun setDisplayEdge(nodeId: Long, item: Item, isInput: Boolean, offset: Offset){
        if(!isEdgeListDisplayed.value) {
            _isEdgeListDisplayed.update { true }
            _nodeId.update { nodeId }
            _item.update { item }
            _edgeListOffset.update { offset }
            edgesList.addAll(if (isInput) getItemInputEdges() else getItemOutputEdges())
        }
    }

    fun updateEdgeListOffset(offset: Offset){
        _edgeListOffset.update { it + offset }
    }

    fun resetEdgeListUpdate(){
        _isEdgeListDisplayed.update { false  }
        _nodeId.update{ null }
        _item.update { null }
        edgesList.clear()
    }

    private fun getItemInputEdges(): List<Edge> {
        val nodeId = _nodeId.value ?: return emptyList()

        val edges = graph.getInputEdges(nodeId)
        val filter = edges.filter { it.item == item.value }

        return filter
    }

    private fun getItemOutputEdges(): List<Edge> {
        val nodeId = _nodeId.value ?: return emptyList()

        val edges = graph.getOutputEdges(nodeId)
        val filter = edges.filter { it.item == item.value }

        return filter
    }

    fun deleteEdge(edgeId: Long){
        graph.removeEdge(edgeId, false)
        edgesList.removeIf { edge -> edge.id == edgeId }
        _state.update {state ->
            state.edges.remove(edgeId)

            state.copy(edges = state.edges)
        }
    }
}