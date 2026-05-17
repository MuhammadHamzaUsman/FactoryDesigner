package save.dto

import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.Serializable

@Serializable
data class OffsetSave (
    val x: Float,
    val y: Float
){
    fun toOffset() = Offset(x, y)
}

fun Offset.toSave() = OffsetSave(x, y)