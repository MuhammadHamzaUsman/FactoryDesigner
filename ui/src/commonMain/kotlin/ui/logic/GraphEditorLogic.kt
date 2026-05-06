package ui.logic

import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import testState
import kotlin.math.pow

class GraphEditorLogic {
    private var _state = MutableStateFlow(testState)
    val state = _state.asStateFlow()

    fun updateZoom(scrollDelta: Float, cursor: Offset){
        _state.update { state ->
            val camera = state.camera
            val oldZoom = camera.zoom

            // Calculate new zoom level
            val newZoom = (oldZoom * 1.05f.pow(-scrollDelta))
                .coerceIn(camera.zoomMin, camera.zoomMax)

            // Find the world position under the cursor before zooming
            // WorldPos = (ScreenPos / Zoom) + Offset
            val worldUnderCursor = (cursor / oldZoom) + camera.offset

            // We want the new world position under the cursor to match the old one
            // ScreenPos / NewZoom + NewOffset = WorldUnderCursor
            // So: NewOffset = WorldUnderCursor - (ScreenPos / NewZoom)
            val newOffset = worldUnderCursor - (cursor / newZoom)

            state.copy(
                camera = camera.copy(
                    zoom = newZoom,
                    offset = newOffset
                )
            )
        }
    }

    fun updateCameraPosition(dragAmount: Offset){
        _state.update{ state ->
            val camera = state.camera
            state.copy(
                camera = camera.copy(
                    offset = camera.offset - (dragAmount / camera.zoom)
                )
            )
        }
    }


    fun updateNodePosition(nodeId: Long, delta: Offset){
        _state.update { state ->
            val node = state.nodes[nodeId] ?: return@update state

            state.copy(
                nodes = state.nodes.toMutableMap().apply {
                    this[nodeId] = node.copy(
                        position = node.position + delta
                    )
                }
            )
        }
    }
}

