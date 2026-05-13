package ui.logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import initGraph
import itemAndRecipe
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Edge
import org.example.graph.GraphException
import org.example.graph.GraphToLinearSystem
import org.example.graph.node.*
import org.example.math.LinearSystemSolver
import testState
import ui.model.FilterOption
import ui.model.UiNode
import ui.model.toUiEdge
import ui.model.toUiNode
import ui.state.GraphMode
import ui.state.MachineCountUpdate
import util.round
import util.toDoubleRoundedStringOrEmpty
import kotlin.math.pow

@OptIn(FlowPreview::class)
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
    val filteredRecipe = searchText
        .debounce(500)
        .combine(filterOption){ searchText, filterOption ->
            if(searchText.isBlank() || filterOption == null) emptyList()
            else getListOfRecipe(searchText, filterOption)
        }

    val filteredItem = searchText
        .debounce(500)
        .combine(filterOption){ searchText, filterOption ->
            if(searchText.isBlank() || filterOption == null) emptyList()
            else getListOfItem(searchText, filterOption)
        }

    fun getListOfItem(searchText: String, filterOption: FilterOption): List<Item> {
        if (filterOption == FilterOption.Source || filterOption == FilterOption.Sink) {

            val lower = searchText.lowercase()

            return itemAndRecipeState.items.values
                .filter {
                    it.name.lowercase().contains(lower)
                }
        }

        return emptyList()
    }

    fun getListOfRecipe(search: String, filterOption: FilterOption): List<Recipe> {
        return when(filterOption){
            FilterOption.RECIPE -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.name.lowercase().contains(search.lowercase())
                }.map { it.value }

            FilterOption.INPUT_MATERIAL -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.inputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }.map { it.value }

            FilterOption.OUTPUT_MATERIAL -> itemAndRecipeState.recipes
                .filter {(_, recipe) ->
                    recipe.outputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }.map { it.value }

            else -> {emptyList()}
        }
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

    fun getMaterialCount(edges: Set<Edge>, items: Set<Item>): LinkedHashMap<Item, Double?>{
        val map = LinkedHashMap<Item, Double?>()
        for (item in items) {
            map[item] = null
        }

        var prev: Double?
        var curr: Double?

        for (edge in edges) {
            prev = map[edge.item] ?: 0.0
            curr = _state.value.edgesValue?.get(edge.id) ?: continue

            map[edge.item] = prev + curr
        }

        return map
    }

    fun getMaterialCount(edges: Set<Edge>, item: Item): LinkedHashMap<Item, Double?>{
        val map = LinkedHashMap<Item, Double?>()
        map[item] = null

        var prev: Double?
        var curr: Double?

        for (edge in edges) {
            prev = map[edge.item] ?: 0.0
            curr = _state.value.edgesValue?.get(edge.id) ?: continue

            map[edge.item] = prev + curr
        }

        return map
    }

    fun getInputMaterials(nodeId: Long): LinkedHashMap<Item, Double?> {
        val edges = graph.getInputEdges(nodeId)
        val items = when(val node = graph.getNode(nodeId)){
            is TransformationNode ->  node.recipe.inputMaterials.keys
            is SourceNode ->  setOf(node.item)
            is MergerNode ->  setOf(node.item)
            is SinkNode ->  setOf(node.item)
            is SplitterNode ->  setOf(node.item)
            else -> emptySet()
        }

        return getMaterialCount(edges, items)
    }

    fun getOutputMaterial(nodeId: Long): LinkedHashMap<Item, Double?> {
        val edges = graph.getOutputEdges(nodeId)
        val items = when(val node = graph.getNode(nodeId)){
            is TransformationNode ->  node.recipe.outputMaterials.keys
            is SourceNode ->  setOf(node.item)
            is MergerNode ->  setOf(node.item)
            is SinkNode ->  setOf(node.item)
            is SplitterNode ->  setOf(node.item)
            else -> emptySet()
        }

        return getMaterialCount(edges, items)
    }

    fun addNode(recipe: Recipe, offset: Offset){
        if(graphMode.value == GraphMode.CALCULATING) return

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
        } else if(mode == GraphMode.CALCULATING){
            resetToNormal()
            return
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
        else if(mode == GraphMode.CALCULATING){
            resetToNormal()
            return
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
        val added = graph.addEdge(tempEdge)

        if(added) {
            pointsList.add(offset)
            val uiEdge = tempEdge!!.toUiEdge(if (asReversed) pointsList.asReversed() else pointsList)

            _state.update { currentState ->
                currentState.edges[uiEdge.id] = uiEdge
                currentState.copy(edges = currentState.edges)
            }
        }

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
        if(graphMode.value == GraphMode.CALCULATING) return

        graph.removeEdge(edgeId, false)
        edgesList.removeIf { edge -> edge.id == edgeId }
        _state.update {state ->
            state.edges.remove(edgeId)

            state.copy(edges = state.edges)
        }
    }

    fun removeNode(uiNode: UiNode) {
        if(graphMode.value == GraphMode.CALCULATING) return

        val removedEdges = graph.removeNode(uiNode.id)
        val edges = state.value.edges
        val nodes = state.value.nodes

        for (edge in removedEdges) {
            edges.remove(edge.id)
        }

        nodes.remove(uiNode.id)

        _state.update { state ->
            state.copy(
                nodes = nodes,
                edges = edges
            )
        }
    }


    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun setMachineCount(nodeId: Long, machines: String){
        _machineCount.update { MachineCountUpdate(machines, nodeId) }
    }

    fun calculateValuesFromMachineCount(nodeId: Long, machines: Double){
        _graphMode.update { GraphMode.CALCULATING }

        try{
            val pair = GraphToLinearSystem.generateLinearSystem(graph)
            val linearSystem = pair.first
            val edgeVariableMap = pair.second

            val recipe = (graph.getNode(nodeId) as TransformationNode).recipe

            graph.forEachInputEdge(nodeId) {
                linearSystem.injectVariablesValue(
                    edgeVariableMap[it.id]!!,
                    recipe.inputMaterials[it.item]!! * machines
                )
            }

            graph.forEachOutputEdge(nodeId) {
                linearSystem.injectVariablesValue(
                    edgeVariableMap[it.id]!!,
                    recipe.outputMaterials[it.item]!! * machines
                )
            }

            val solved = LinearSystemSolver.solveSystem(linearSystem)

            val newEdgeValues = edgeVariableMap.mapValues { solved[it.value]?.round(2) ?: 0.0 }
            calculateMachineCounts(newEdgeValues).forEach { (id, double) ->
                _state.value.machineCount[id] = double.toDoubleRoundedStringOrEmpty(2)
            }

            _state.update { state ->
                state.copy(edgesValue = newEdgeValues)
            }

            println(newEdgeValues)
        }
        catch (e: GraphException){ }
        finally {
            _graphMode.update { GraphMode.NORMAL }
        }
    }

    private fun calculateMachineCounts(edgeValues: Map<Long, Double>): Map<Long, Double> {
        val nodeMachineMap = mutableMapOf<Long, Double>()
        val machineCounts = mutableListOf<Double>()
        var recipe: Recipe

        graph.forEachNode { node ->
            if(node is TransformationNode){
                recipe = node.recipe

                graph.forEachInputEdge(node.id){ edge ->
                    machineCounts.add(edgeValues[edge.id]!! / recipe.inputMaterials[edge.item]!!)
                }

                graph.forEachOutputEdge(node.id){ edge ->
                    machineCounts.add(edgeValues[edge.id]!! / recipe.outputMaterials[edge.item]!!)
                }

                if(machineCounts.isNotEmpty()) {
                    nodeMachineMap[node.id] = machineCounts.sum() / machineCounts.size
                }
                machineCounts.clear()
            }
        }

        return nodeMachineMap
    }

    fun clearScope(){
        scope.cancel()
    }

    fun addNode(item: Item, offset: Offset, isSource: Boolean) {
        if(graphMode.value == GraphMode.CALCULATING) return

        _state.update { state ->
            val node = if(isSource) SourceNode(item) else SinkNode(item)
            graph.addNode(node)

            state.nodes[node.id] = node.toUiNode(offset)

            state
        }
    }

    private val _machineCount = MutableStateFlow<MachineCountUpdate?>(null)
    val machineCount = _machineCount.asStateFlow()

    init{
        scope.launch {
            machineCount
                .debounce(500)
                .filterNotNull()
                .collectLatest { update ->
                    val count = update.count.toDoubleOrNull() ?: return@collectLatest
                    calculateValuesFromMachineCount(update.nodeId, count)
                }
        }
    }

    fun getNode(uiNodeId: Long): Node = graph.getNode(uiNodeId)
}