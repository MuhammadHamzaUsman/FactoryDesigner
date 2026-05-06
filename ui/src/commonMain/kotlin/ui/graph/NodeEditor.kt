package ui.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import ui.screen.NodeCard

@Composable
fun GraphScreen(
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
){

    val state by controller.state.collectAsState()
    val camera = state.camera

    Box(
        modifier = modifier
            .fillMaxSize()
            .panZoom(controller)
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
    }
}