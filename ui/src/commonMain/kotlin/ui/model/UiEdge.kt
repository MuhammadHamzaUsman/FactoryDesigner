package ui.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.plus
import org.example.graph.Edge
import org.example.graph.Graph
import ui.state.GraphEditorLayoutState
import util.deepCopy

data class UiEdge(
    val id: Long,
    val points: MutableList<Offset>
){
    fun updateFirstOffset(delta: Offset){
        if(points.isNotEmpty()) {
            points[0] = points[0] + delta
        }
    }
    fun updateLastOffset(delta: Offset) {
        if(points.isNotEmpty()) {
            points[points.lastIndex] = points[points.lastIndex] + delta
        }
    }

    fun Graph.toEdgeUIList(graphLayout: GraphEditorLayoutState): MutableMap<Long, UiEdge> = edges.associate {
        it.id to it.toUiEdge(graphLayout.edges[it.id]?.points?.deepCopy() ?: mutableListOf(Offset.Zero))
    } as MutableMap<Long, UiEdge>

}

fun Edge.toUiEdge(points: MutableList<Offset>): UiEdge = UiEdge(id, points.deepCopy())

