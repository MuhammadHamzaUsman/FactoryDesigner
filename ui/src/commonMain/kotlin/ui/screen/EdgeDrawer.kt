package ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import ui.logic.GraphEditorLogic

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EdgeDrawer(
    controller: GraphEditorLogic,
    width: Float = 3f,
    color: Color,
    modifier: Modifier = Modifier
){
    val state by controller.state.collectAsState()
    val edges = state.edges.values.toList()
    val tempPoints = controller.pointsList

    Canvas(modifier = modifier) {
        for (edge in edges) {
            if (edge.points.size >= 2) {
                drawEdge(edge.points, color, width)
            }
        }

        if (tempPoints.isNotEmpty()) {
            drawEdge(tempPoints.toList(), color, width)
        }
    }

}

private fun DrawScope.drawEdge(
    edge: List<Offset>,
    color: Color,
    width: Float
) {
    for (i in 0 until (edge.size - 1)) {
        drawLine(
            color = color,
            strokeWidth = width,
            start = edge[i],
            end = edge[i + 1],
            cap = StrokeCap.Round
        )
    }
}