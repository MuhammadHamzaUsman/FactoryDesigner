package ui.state

import ui.model.Camera
import ui.model.UiEdge
import ui.model.UiNode

data class GraphEditorLayoutState(
    val nodes: MutableMap<Long, UiNode>,
    val edges: MutableMap<Long, UiEdge>,
    val camera: Camera
)
