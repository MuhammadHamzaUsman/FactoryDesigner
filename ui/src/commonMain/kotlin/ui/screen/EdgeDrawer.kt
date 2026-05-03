package ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import ui.state.GraphEditorLayoutState
import util.worldToScreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EdgeDrawer(
    editorLayoutState: GraphEditorLayoutState,
    width: Float = 3f,
    color: Color,
    modifier: Modifier = Modifier
){
    Canvas(
        modifier = modifier
    ){
        for (edge in editorLayoutState.edges.values) {
            val points = edge.points

            if(points.size < 2) continue

            for(i in 0 until (edge.points.size - 1)){
                drawLine(
                    color = color,
                    strokeWidth = width,
                    start = edge.points[i].worldToScreen(editorLayoutState.camera),
                    end = edge.points[i + 1].worldToScreen(editorLayoutState.camera),
                    cap = StrokeCap.Round
                )
            }
        }
    }
}