package ui.model

import androidx.compose.ui.geometry.Offset
import org.example.graph.Graph
import org.example.graph.node.Node
import org.example.graph.node.NodeType
import ui.state.GraphEditorLayoutState

data class UiNode(
    val id: Long,
    val position: Offset,
    val type: NodeType = NodeType.TRANSFORMATION
)

fun Node.toUiNode(position: Offset): UiNode = UiNode(id, position, type)

fun Graph.toNodeUIList(graphLayout: GraphEditorLayoutState): MutableMap<Long, UiNode> = nodes.associate {
    it.id to it.toUiNode(graphLayout.nodes[it.id]?.position ?: Offset.Zero)
} as MutableMap<Long, UiNode>