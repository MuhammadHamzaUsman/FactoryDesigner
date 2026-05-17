package ui.logic

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Edge
import org.example.graph.Graph
import org.example.graph.GraphException
import org.example.graph.GraphToLinearSystem
import org.example.graph.node.*
import org.example.math.LinearSystem
import org.example.math.LinearSystemSolver
import org.example.math.Variable
import save.AppSave
import save.dto.CountersSave
import save.dto.toSave
import ui.model.FilterOption
import ui.model.UiNode
import ui.model.toUiEdge
import ui.model.toUiNode
import ui.state.GraphEditorLayoutState
import ui.state.GraphMode
import ui.state.ItemCountUpdate
import ui.state.MachineCountUpdate
import util.round
import util.toDoubleRoundedStringOrEmpty
import kotlin.math.pow

@OptIn(FlowPreview::class)
class GraphEditorLogic(
    private val graph: Graph,
    itemAndRecipeState: ItemAndRecipeState,
    graphEditorLayoutState: GraphEditorLayoutState
) {
    private var _state = MutableStateFlow(graphEditorLayoutState)
    val state = _state.asStateFlow()

    private val _itemAndRecipeState = MutableStateFlow(itemAndRecipeState)
    val itemAndRecipeState = _itemAndRecipeState.asStateFlow()

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
        return when(filterOption){
            FilterOption.Source, FilterOption.Sink, FilterOption.SPLITTER, FilterOption.MERGER ->{
                val lower = searchText.lowercase()

                itemAndRecipeState.value.items.values
                    .filter {
                        it.name.lowercase().contains(lower)
                    }
            }
            else -> emptyList()
        }
    }

    fun getListOfRecipe(search: String, filterOption: FilterOption): List<Recipe> {
        return when(filterOption){
            FilterOption.RECIPE -> itemAndRecipeState.value.recipes
                .filter {(_, recipe) ->
                    recipe.name.lowercase().contains(search.lowercase())
                }.map { it.value }

            FilterOption.INPUT_MATERIAL -> itemAndRecipeState.value.recipes
                .filter {(_, recipe) ->
                    recipe.inputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }.map { it.value }

            FilterOption.OUTPUT_MATERIAL -> itemAndRecipeState.value.recipes
                .filter {(_, recipe) ->
                    recipe.outputMaterials.keys.any { it.name.lowercase().contains(search.lowercase()) }
                }.map { it.value }

            else -> {emptyList()}
        }
    }

    fun getNodeName(nodeId: Long): String{
        return when(val node = graph.getNode(nodeId)){
            is TransformationNode -> node.recipe.name
            is SplitterNode -> "${node.item.name} - Splitter"
            is MergerNode -> "${node.item.name} - Merger"
            is SourceNode -> "${node.item.name} - Source"
            is SinkNode -> "${node.item.name} - Sink"
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
        } else{
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
        else{
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
        if(graphMode.value == GraphMode.CALCULATING) return
        pointsList.add(offset)
    }

    val pointsList = SnapshotStateList<Offset>()
    private var tempEdge: Edge? = null

    private var _isEdgeListDisplayed = MutableStateFlow(false)
    val isEdgeListDisplayed = _isEdgeListDisplayed.asStateFlow()

    private val _edgeListOffset = MutableStateFlow(Offset(0f, 0f))
    val edgeListOffset = _edgeListOffset.asStateFlow()

    private val _nodeId = MutableStateFlow<Long?>(null)

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
        if(graphMode.value == GraphMode.CALCULATING) return

        _machineCount.update { MachineCountUpdate(machines, nodeId) }
    }

    fun calculateValuesFromMachineCount(nodeId: Long, machines: Double){
        _graphMode.update { GraphMode.CALCULATING }
        try{
            val node = graph.getNode(nodeId)

            if(node !is TransformationNode) return

            val (linearSystem, edgeVariableMap) = GraphToLinearSystem.generateLinearSystem(graph)
            val recipe = node.recipe

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

            executeSolverAndUpdate(linearSystem, edgeVariableMap)
        }
        catch (_: GraphException){ }
        finally {
            _graphMode.update { GraphMode.NORMAL }
        }
    }

    private fun executeSolverAndUpdate(linearSystem: LinearSystem, edgeVariableMap: Map<Long, Variable>){
        val solved = LinearSystemSolver.solveSystem(linearSystem)

        val newEdgeValues = edgeVariableMap.mapValues { solved[it.value]?.round(2) ?: 0.0 }
        calculateMachineCounts(newEdgeValues).forEach { (id, double) ->
            _state.value.machineCount[id] = double.toDoubleRoundedStringOrEmpty(2)
        }

        _state.update { state ->
            state.copy(edgesValue = newEdgeValues)
        }
    }

    private fun calculateMachineCounts(edgeValues: Map<Long, Double>): Map<Long, Double> {
        val nodeMachineMap = mutableMapOf<Long, Double>()
        val machineCounts = mutableListOf<Double>()
        var recipe: Recipe

        graph.forEachNode { node ->
            when(node) {
                is TransformationNode -> {
                    recipe = node.recipe

                    graph.forEachInputEdge(node.id) { edge ->
                        machineCounts.add(edgeValues[edge.id]!! / recipe.inputMaterials[edge.item]!!)
                    }

                    graph.forEachOutputEdge(node.id) { edge ->
                        machineCounts.add(edgeValues[edge.id]!! / recipe.outputMaterials[edge.item]!!)
                    }

                    if (machineCounts.isNotEmpty()) {
                        nodeMachineMap[node.id] = machineCounts.sum() / machineCounts.size
                    }
                    machineCounts.clear()
                }
            }
        }

        return nodeMachineMap
    }

    fun addNode(item: Item, offset: Offset, type: NodeType) {
        if(graphMode.value == GraphMode.CALCULATING) return

        _state.update { state ->
            val node = when(type){
                NodeType.SOURCE -> SourceNode(item)
                NodeType.SINK -> SinkNode(item)
                NodeType.SPLITTER -> SplitterNode(item)
                NodeType.MERGER -> MergerNode(item)
                else -> return
            }

            graph.addNode(node)

            state.nodes[node.id] = node.toUiNode(offset)

            state
        }
    }

    private val _machineCount = MutableStateFlow<MachineCountUpdate?>(null)
    val machineCount = _machineCount.asStateFlow()

    fun getNode(uiNodeId: Long): Node = graph.getNode(uiNodeId)

    private var _itemCount = MutableStateFlow<ItemCountUpdate?>(null)
    val itemCount = _itemCount.asStateFlow()

    fun setItemCount(uiNodeId: Long, item: Item, itemCount: String, isInput: Boolean){
        if(graphMode.value == GraphMode.CALCULATING) return

        _itemCount.update { ItemCountUpdate(uiNodeId, item, itemCount, isInput) }
    }

    fun calculateValuesFromItemCount(uiNodeId: Long, item: Item, itemCount: Double, isInput: Boolean) {
        when(val node = graph.getNode(uiNodeId)){
            is TransformationNode -> {
                val recipe = node.recipe
                val materialCount = if(isInput) recipe.inputMaterials else recipe.outputMaterials
                val machineCount = itemCount / (materialCount[item] ?: return)

                setMachineCount(uiNodeId, machineCount.toString())
            }

            is SinkNode -> {
                val inputEdges = graph.getInputEdges(node.id)

                calculateFromSinkSourceNode(item, inputEdges, itemCount)
            }

            is SourceNode -> {
                val outputEdges = graph.getOutputEdges(node.id)

                calculateFromSinkSourceNode(item, outputEdges, itemCount)
            }
        }
    }

    private fun calculateFromSinkSourceNode(item: Item, edges: Set<Edge>, count: Double){
        _graphMode.update { GraphMode.CALCULATING }

        val (linearSystem, edgeVariableMap) = GraphToLinearSystem.generateLinearSystem(graph)

        val edge = edges.find { edge ->
            edge.item == item
        } ?: return

        linearSystem.injectVariablesValue(edgeVariableMap[edge.id]!!, count)

        executeSolverAndUpdate(linearSystem, edgeVariableMap)

        _graphMode.update { GraphMode.NORMAL }
    }

    fun updateEdgeWeight(edgeId: Long, weight: Float) {
        graph.getEdge(edgeId).weight = weight
    }

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

        scope.launch {
            itemCount
                .debounce(500)
                .filterNotNull()
                .collectLatest { update ->
                    val count = update.itemCount.toDoubleOrNull() ?: return@collectLatest
                    calculateValuesFromItemCount(update.nodeId, update.item, count, update.isInput)
                }
        }
    }

    fun generateError(){
        _graphMode.update { GraphMode.ERROR }
    }

    fun okError() {
        _graphMode.update { GraphMode.NORMAL }
    }

    fun generateAppSave(): AppSave {
        _graphMode.update { GraphMode.IO }

        val save = AppSave(
            CountersSave.createFromCurrentValues(),
            graph.toSave(),
            state.value.toSave(),
            itemAndRecipeState.value.toSave()
        )

        _graphMode.update { GraphMode.NORMAL }
        return save
    }

    // only take those recipes and items that have unique case-sensitive name
    fun mergeItemAndRecipeState(newItemAndRecipeState: ItemAndRecipeState) {
        _graphMode.update { GraphMode.IO }
        val itemAndRecipeState = itemAndRecipeState.value
        val itemNames = itemAndRecipeState.items.values.mapTo(mutableSetOf()){ it.name }

        for (item in newItemAndRecipeState.items.values) {
            if(item.name in itemNames) continue

            itemAndRecipeState.items[item.id] = item
            itemNames.add(item.name)
        }

        val recipeNames = itemAndRecipeState.recipes.values.mapTo(mutableSetOf()){ it.name }

        for (recipe in newItemAndRecipeState.recipes.values) {
            if(recipe.name in recipeNames) continue

            itemAndRecipeState.recipes[recipe.id] = recipe
            recipeNames.add(recipe.name)
        }

        _graphMode.update { GraphMode.NORMAL }
    }
}