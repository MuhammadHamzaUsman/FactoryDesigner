package save.dto

import kotlinx.serialization.Serializable
import ui.model.UiEdge

@Serializable
data class UiEdgeSave(
    val id: Long,
    val points: List<OffsetSave>
){
    fun toEdgeUi() = UiEdge(
        id,
         MutableList(points.size){
             points[it].toOffset()
         }
    )
}

fun UiEdge.toSave() = UiEdgeSave(id, points.map { it.toSave() })