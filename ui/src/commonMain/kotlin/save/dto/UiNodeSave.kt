package save.dto

import kotlinx.serialization.Serializable
import org.example.graph.node.NodeType
import ui.model.UiNode

@Serializable
data class UiNodeSave(
    val id: Long,
    val position: OffsetSave,
    val type: NodeType
){
    fun toUiNode() = UiNode(id, position.toOffset(), type)
}

fun UiNode.toSave() = UiNodeSave(id, position.toSave(), type)