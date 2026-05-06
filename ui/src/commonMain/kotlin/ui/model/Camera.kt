package ui.model

import androidx.compose.ui.geometry.Offset

data class Camera(
    val offset: Offset,
    var zoom: Float,
    var zoomSensitivity: Float = 0.5f,
    val zoomMin: Float = 0.5f,
    val zoomMax: Float = 10f
)