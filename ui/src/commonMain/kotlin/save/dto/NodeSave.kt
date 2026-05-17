package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.node.MergerNode
import org.example.graph.node.Node
import org.example.graph.node.NodeType
import org.example.graph.node.SinkNode
import org.example.graph.node.SourceNode
import org.example.graph.node.SplitterNode
import org.example.graph.node.TransformationNode

@Serializable
data class NodeSave(
    val id: Long,
    val type: NodeType,
    val item: Long?, // item id
    val recipe: Long? // recipe: id
){
    fun toNode(items: Map<Long, Item>, recipes: Map<Long, Recipe>) = when(type){
        NodeType.SOURCE -> SourceNode(id, items[item] ?: throw IllegalStateException("Source Node: Item $item not found in items map."))
        NodeType.SINK -> SinkNode(id, items[item] ?: throw IllegalStateException("Sink Node: Item $item not found in items map."))
        NodeType.TRANSFORMATION -> TransformationNode(id, recipes[recipe] ?: throw IllegalStateException("Transformation Node: Recipe $recipe not found in recipes map."))
        NodeType.SPLITTER -> SplitterNode(id, items[item] ?: throw IllegalStateException("Splitter Node: Item $item not found in items map."))
        NodeType.MERGER -> MergerNode(id, items[item] ?: throw IllegalStateException("Merger Node: Item $item not found in items map."))
    }
}

fun Node.toSave() = when(this) {
    is SourceNode -> NodeSave(id, NodeType.SOURCE, item.id, null)
    is SinkNode -> NodeSave(id, NodeType.SINK, item.id, null)
    is TransformationNode -> NodeSave(id, NodeType.TRANSFORMATION, null, recipe.id)
    is SplitterNode -> NodeSave(id, NodeType.SPLITTER, item.id, null)
    is MergerNode -> NodeSave(id, NodeType.MERGER, item.id, null)
    else -> throw IllegalStateException("Unknown found during serialization ")
}