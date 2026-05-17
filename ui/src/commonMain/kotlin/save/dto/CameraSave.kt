package save.dto

import kotlinx.serialization.Serializable
import ui.model.Camera

@Serializable
data class CameraSave (
    val offset: OffsetSave,
    val zoom: Float
){
    fun toCamera() = Camera(offset.toOffset(), zoom)
}

fun Camera.toSave() = CameraSave(offset.toSave(), zoom)