package ui.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.gestures.rememberDraggable2DState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import inputMaterial
import machineCount
import nameFromId
import org.example.factory.Item
import outputMaterial
import ui.logic.GraphEditorLogic
import ui.model.UiNode
import ui.modifier.panZoom
import ui.screen.MachineSelectionMenu
import ui.screen.NodeCard
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .panZoom(controller)
            .clickable(
                onClick = { controller.setRecipeMenuDisplayed(!isRecipeMenuDisplayed) }
            )
            .graphicsLayer {
                transformOrigin = TransformOrigin(0f, 0f)
                scaleX = camera.zoom
                scaleY = camera.zoom
                translationX = -camera.offset.x * camera.zoom
                translationY = -camera.offset.y * camera.zoom
            }
    ){
        Canvas(
            contentDescription = "",
            modifier = modifier.fillMaxSize()
        ) { }

        for ((id, node) in state.nodes) {
            NodeCard(
                uiNode = node,
                nodeName = nameFromId(id),
                nodeCount = machineCount(id).toString(),
                inputMaterialCount = inputMaterial(id),
                outputMaterialCount = outputMaterial(id),
                onMachineCountValueChange = { node: UiNode, d: Double?, offset: Offset -> },
                onInputMaterialCountChange = { node: UiNode, item: Item, d: Double?, offset: Offset -> },
                onOutputMaterialCountChange = { node: UiNode, item: Item, d: Double?, offset: Offset -> },
                controller = controller
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