package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item

@Serializable
data class ItemSave (
    val id: Long,
    val name: String
){
    fun toItem() = Item(id, name)

}

fun Item.toSave() = ItemSave(id, name)