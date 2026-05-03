package ui.model

import androidx.compose.ui.geometry.Offset

data class Camera(
    val offset: Offset,
    var zoom: Float
) {
}