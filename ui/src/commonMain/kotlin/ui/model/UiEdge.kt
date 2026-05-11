package ui.model

import androidx.compose.ui.geometry.Offset
import org.example.graph.Edge
import org.example.graph.Graph
import ui.state.GraphEditorLayoutState

data class UiEdge(
    val id: Long,
    val points: List<Offset>
)

fun Edge.toUiEdge(points: List<Offset>): UiEdge = UiEdge(id, points)

fun Graph.toEdgeUIList(graphLayout: GraphEditorLayoutState): MutableMap<Long, UiEdge> = edges.associate {
    it.id to it.toUiEdge(graphLayout.edges[it.id]?.points ?: listOf(Offset.Zero))
} as MutableMap<Long, UiEdge>