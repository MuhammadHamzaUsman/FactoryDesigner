package save.dto

import androidx.compose.runtime.snapshots.SnapshotStateMap
import kotlinx.serialization.Serializable
import ui.model.UiEdge
import ui.model.UiNode
import ui.state.GraphEditorLayoutState

@Serializable
data class GraphEditorLayoutStateSave(
    val nodes: List<UiNodeSave>,
    val edges: List<UiEdgeSave>,
    val camera: CameraSave,
    val edgesValue: Map<Long, Double>?,
    val machineCount: Map<Long, String>
){
    fun toGraphEditorLayoutState(): GraphEditorLayoutState {
        val _nodes = SnapshotStateMap<Long, UiNode>().apply {
            for (save in nodes) {
                put(save.id, save.toUiNode())
            }
        }

        val _edges = SnapshotStateMap<Long, UiEdge>().apply {
            for (save in edges) {
                put(save.id, save.toEdgeUi())
            }
        }

        val _machineCount = SnapshotStateMap<Long, String>().apply {
            for ((nodeId, count) in machineCount) {
                put(nodeId, count)
            }
        }
        
        return GraphEditorLayoutState(_nodes, _edges, camera.toCamera(), edgesValue, _machineCount)
    }
}

fun GraphEditorLayoutState.toSave() = GraphEditorLayoutStateSave(
    nodes = nodes.values.map { it.toSave() },
    edges = edges.values.map { it.toSave() },
    camera = camera.toSave(),
    edgesValue = edgesValue,
    machineCount = buildMap {
        for ((nodeID, count) in machineCount) {
            put(nodeID, count)
        }
    }
)