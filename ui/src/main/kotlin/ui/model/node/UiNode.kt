package ui.model.node

import androidx.compose.ui.geometry.Offset
import org.example.graph.Graph
import org.example.graph.node.Node
import org.example.graph.node.NodeType

data class UiNode(
    val id: Long,
    val position: Offset,
    val type: NodeType = NodeType.TRANSFORMATION
)

fun Node.toUiNode(position: Offset): UiNode = UiNode(id, position, type)

fun Graph.toUI(positionMap: NodePositionMap): List<UiNode> = nodes.mapNotNull {
    it?.toUiNode(positionMap.get(it.id))
}.toList()