package ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import ui.logic.GraphEditorLogic
import ui.modifier.panZoom
import util.worldToScreen

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
        for (edge in controller.state.value.edges.values) {
            val points = edge.points

            if(points.size < 2) continue

            for(i in 0 until (edge.points.size - 1)){
                drawLine(
                    color = color,
                    strokeWidth = width,
                    start = edge.points[i].worldToScreen(controller.state.value.camera),
                    end = edge.points[i + 1].worldToScreen(controller.state.value.camera),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}