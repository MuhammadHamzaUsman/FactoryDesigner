package ui.graph

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.logic.GraphEditorLogic
import ui.modifier.panZoom
import ui.screen.*
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

    val errorMessage by controller.errorMessage.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
                )
                .padding(4.dp, 0.dp, 4.dp, 4.dp)
        ) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = "Load Recipe and Items",
                fontWeight = FontWeight.Bold,
                lineHeight = 16.sp,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .clickable{
                        controller.loadItemsAndRecipes()
                    }
                    .background(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp)
                    )
                    .padding(4.dp)
            )
        }

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

            errorMessage?.let {
                Error(
                    message = it,
                    modifier = Modifier.align(Alignment.Center)
                ){
                    controller.okError()
                }
            }
        }
    }
}