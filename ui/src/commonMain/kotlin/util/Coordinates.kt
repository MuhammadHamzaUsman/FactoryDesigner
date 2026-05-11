package util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import ui.model.Camera
import kotlin.math.roundToInt

fun Offset.worldToScreen(camera: Camera): Offset {
    return Offset(
        x = (this.x - camera.offset.x) * camera.zoom,
        y = (this.y - camera.offset.y) * camera.zoom
    )
}

fun Offset.screenToWorld(camera: Camera): Offset {
    return Offset(
        x = (this.x / camera.zoom) + camera.offset.x,
        y = (this.y / camera.zoom) + camera.offset.y
    )
}

fun Offset.toIntOffset() = IntOffset(x.roundToInt(), y.roundToInt());