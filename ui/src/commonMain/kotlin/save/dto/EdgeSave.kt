package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item
import org.example.graph.Edge
import org.example.graph.node.Node

@Serializable
data class EdgeSave(
    val id: Long,
    val source: Long,
    val item: Long,
    val destination: Long,
    val weight: Float
){
    fun toEdge(source: Node, items: Map<Long, Item>, destination: Node) = Edge(
        id,
        source,
        items[item] ?: throw IllegalStateException("item of edgeId[$id] not found during deserialization"),
        destination
    )
}

fun Edge.toSave() = EdgeSave(id, source.id, item.id, destination.id, weight)
