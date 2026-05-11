package ui.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import machineCount
import org.example.factory.Item
import ui.logic.GraphEditorLogic
import ui.model.UiNode
import ui.modifier.panZoom
import ui.screen.EdgeDrawer
import ui.screen.MachineSelectionMenu
import ui.screen.NodeCard
import ui.state.GraphMode
import util.screenToWorld
import util.toIntOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GraphScreen(
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
){
    val state by controller.state.collectAsState()

    val isRecipeMenuDisplayed by controller.isRecipeMenuDisplayed.collectAsState()
    val recipeMenuOffset by controller.recipeMenuOffset.collectAsState()
    val draggableState = rememberDraggable2DState{
        offset -> controller.updateRecipeMenuOffset(offset)
        recipeMenuOffset
    }

    val camera = state.camera
    val mode by controller.graphMode.collectAsState()
    var layoutCords: LayoutCoordinates? by remember { mutableStateOf(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCords = it }
            .panZoom(controller)
            .pointerInput(camera, mode) {
                detectTapGestures { localOffset ->
                    val worldOffset = localOffset.screenToWorld(camera)

                    if (mode == GraphMode.EDGE_DRAWING) {
                        controller.addPointToEdge(worldOffset)
                    } else {
                        controller.setRecipeMenuDisplayed(!isRecipeMenuDisplayed)
                    }
                }
            }
            .graphicsLayer {
                scaleX = camera.zoom
                scaleY = camera.zoom
                translationX = -camera.offset.x * camera.zoom
                translationY = -camera.offset.y * camera.zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
    ){
        EdgeDrawer(
            controller = controller,
            width = 3f,
            color = Color.Black,
             modifier = Modifier.fillMaxSize()
        )

        for ((id, node) in state.nodes) {
            NodeCard(
                uiNode = node,
                nodeName = controller.getNodeName(id),
                nodeCount = machineCount(id).toString(),
                inputMaterialCount = controller.getInputMaterials(id) ,
                outputMaterialCount = controller.getOutputMaterial(id),
                onMachineCountValueChange = { node: UiNode, d: Double?, offset: Offset -> },
                onInputMaterialCountChange = { node: UiNode, item: Item, d: Double?, offset: Offset -> },
                onOutputMaterialCountChange = { node: UiNode, item: Item, d: Double?, offset: Offset -> },
                controller = controller,
                containerCords = layoutCords,
            )
        }

        if (isRecipeMenuDisplayed){
            MachineSelectionMenu(
                controller = controller,
                modifier = Modifier
                    .offset{ recipeMenuOffset.toIntOffset() }
                    .draggable2D(draggableState)
            )
        }
    }
}