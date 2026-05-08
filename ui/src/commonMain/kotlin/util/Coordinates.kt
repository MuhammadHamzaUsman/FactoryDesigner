package util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import ui.model.Camera
import kotlin.math.roundToInt

fun Offset.worldToScreen(camera: Camera): Offset {
    return Offset(
        (x - camera.offset.x) * camera.zoom,
        (camera.offset.y - y) * camera.zoom
    )
}

fun Offset.screenToWorld(camera: Camera): Offset {
    return Offset(
        (x / camera.zoom) + camera.offset.x,
        camera.offset.y - (y / camera.zoom)
    )
}

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt());