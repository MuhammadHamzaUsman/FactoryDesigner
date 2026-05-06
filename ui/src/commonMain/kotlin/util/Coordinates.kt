package util

import androidx.compose.ui.geometry.Offset
import ui.model.Camera

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