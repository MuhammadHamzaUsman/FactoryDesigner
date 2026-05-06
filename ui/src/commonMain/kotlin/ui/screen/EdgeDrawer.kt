package ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import ui.logic.GraphEditorLogic
import ui.model.Camera
import ui.modifier.panZoom
import ui.state.GraphEditorLayoutState
import ui.theme.scrimLight
import util.worldToScreen
import kotlin.math.abs
import kotlin.math.pow

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EdgeDrawer(
    controller: GraphEditorLogic,
    width: Float = 3f,
    color: Color,
    modifier: Modifier = Modifier
){

    Canvas(
        modifier = modifier.panZoom(controller)
    ){
        for (edge in controller.state.edges.values) {
            val points = edge.points

            if(points.size < 2) continue

            for(i in 0 until (edge.points.size - 1)){
                drawLine(
                    color = color,
                    strokeWidth = width,
                    start = edge.points[i].worldToScreen(controller.state.camera),
                    end = edge.points[i + 1].worldToScreen(controller.state.camera),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}