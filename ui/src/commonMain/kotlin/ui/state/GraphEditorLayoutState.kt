package ui.state

import androidx.compose.runtime.snapshots.SnapshotStateMap
import ui.model.Camera
import ui.model.UiEdge
import ui.model.UiNode

data class GraphEditorLayoutState(
    val nodes: SnapshotStateMap<Long, UiNode>,
    val edges: SnapshotStateMap<Long, UiEdge>,
    val camera: Camera
)
