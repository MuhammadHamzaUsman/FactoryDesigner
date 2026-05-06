package ui.logic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import ui.model.Camera
import ui.model.UiEdge
import ui.state.GraphEditorLayoutState
import kotlin.math.pow

class GraphEditorLogic {
    var state by mutableStateOf(
        GraphEditorLayoutState(
            nodes = mutableMapOf(),
            edges = mutableMapOf(
                0L to UiEdge(0L, listOf(
                    Offset(1f, 1f),
                    Offset(1f, 10f),
                    Offset(20f, 10f),
                    Offset(20f, 30f),
                    Offset(40f, 30f),
                    Offset(40f, 40f)
                ))
            ),
            camera = Camera(Offset.Zero, 1f)
        )
    )
    private set

    fun updateZoom(scrollDelta: Float, cursorPositon: Offset){
        val camera = state.camera
        val zoomFactor = 1.05f.pow(-scrollDelta)

        val zoom = (camera.zoom * zoomFactor).coerceIn(
            camera.zoomMin,
            camera.zoomMax
        )

        val cursorPosBefore = (cursorPositon / camera.zoom) + camera.offset
        val cursorPosAfter = (cursorPositon / zoom) + camera.offset

        state = state.copy(
            camera = state.camera.copy(
                zoom = zoom,
                offset = camera.offset + (cursorPosBefore - cursorPosAfter)
            )
        )
    }

    fun updateCameraPosition(dragAmount: Offset){
        val camera = state.camera

        state = state.copy(
            camera = camera.copy(
                offset = camera.offset - (dragAmount / camera.zoom)
            )
        )
    }
}