package util

import androidx.compose.ui.geometry.Offset
import ui.model.Camera

fun Offset.worldToScreen(camera: Camera): Offset {
    return (this - camera.offset) * camera.zoom
}

fun Offset.screenToWorld(camera: Camera): Offset {
    return (this / camera.zoom) + camera.offset
}