package util

import androidx.compose.ui.geometry.Offset
import ui.model.Camera

fun Offset.worldToScreen(camera: Camera): Offset {
    return Offset(
        (x - camera.offset.x) * camera.zoom,
        (camera.offset.y - y) * camera.zoom
    )
}


fun Offset.worldToScreen(refence: Offset, zoom: Float): Offset {
    return Offset(
        (x - refence.x) * zoom,
        (refence.y - y) * zoom
    )
}

fun Offset.screenToWorld(camera: Camera): Offset {
    return Offset(
        (x / camera.zoom) + camera.offset.x,
        camera.offset.y - (y / camera.zoom)
    )
}

fun Offset.screenToWorld(refence: Offset, zoom: Float): Offset {
    return Offset(
        (x / zoom) + refence.x,
        refence.y - (y / zoom)
    )
}