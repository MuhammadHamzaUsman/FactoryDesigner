package ui.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import ui.logic.GraphEditorLogic
import ui.modifier.panZoom
import ui.screen.*
import ui.state.AppState
import ui.state.GraphMode
import util.screenToWorld
import util.toIntOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NodeEditor(
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
){
    val state by controller.state.collectAsState()

    val isRecipeMenuDisplayed by controller.isRecipeMenuDisplayed.collectAsState()
    val recipeMenuOffset by controller.recipeMenuOffset.collectAsState()
    val draggableStateRecipeMenu = rememberDraggable2DState{ offset ->
        controller.updateRecipeMenuOffset(offset)
        recipeMenuOffset
    }

    val isEdgeListDisplayed by controller.isEdgeListDisplayed.collectAsState()
    val edgeList = controller.edgesList
    val edgeListOffset by controller.edgeListOffset.collectAsState()
    val draggableStateEdgeList = rememberDraggable2DState{ offset ->
        controller.updateEdgeListOffset(offset)
        edgeListOffset
    }

    val camera = state.camera
    val mode by controller.graphMode.collectAsState()
    var layoutCords: LayoutCoordinates? by remember { mutableStateOf(null) }


    Column(
        modifier = Modifier.fillMaxSize()
    ){
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
        ) {
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
                    inputMaterialCount = controller.getInputMaterials(id),
                    outputMaterialCount = controller.getOutputMaterial(id),
                    controller = controller,
                    containerCords = layoutCords,
                )
            }

            if (isRecipeMenuDisplayed) {
                MachineSelectionMenu(
                    controller = controller,
                    modifier = Modifier
                        .offset { recipeMenuOffset.toIntOffset() }
                        .draggable2D(draggableStateRecipeMenu)
                )
            }

            if (isEdgeListDisplayed) {
                EdgesList(
                    edgeList,
                    controller,
                    modifier = Modifier
                        .offset { edgeListOffset.toIntOffset() }
                        .draggable2D(draggableStateEdgeList)
                )
            }
        }
    }
}