package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item
import org.example.factory.Recipe

@Serializable
data class RecipeSave(
    val id: Long,
    val name: String,
    val machineName: String,
    val inputMaterials: Map<Long, Double>,
    val outputMaterials: Map<Long, Double>,
    val primaryOutput: Long // primaryOutput id
){
    fun toRecipe(itemsMap: Map<Long, Item>): Recipe{
        val _inputMaterials = LinkedHashMap<Item, Double>()

        for ((itemId, count) in inputMaterials) {
            _inputMaterials[itemsMap[itemId] ?: throw IllegalStateException("Input Item not in items map")] = count
        }

        val _outputMaterials = LinkedHashMap<Item, Double>()

        for ((itemId, count) in outputMaterials) {
            _outputMaterials[itemsMap[itemId] ?: throw IllegalStateException("Output Item not in items map")] = count
        }

        return Recipe(
            id, name, machineName, _inputMaterials, _outputMaterials, itemsMap[primaryOutput]  ?: throw IllegalStateException("Primary Output Item not in items map")
        )
    }
}

fun Recipe.toSave(): RecipeSave {
    val _inputMaterials = buildMap {
        for ((item, count) in inputMaterials) {
            put(item.id, count)
        }
    }

    val _outputMaterials = buildMap {
        for ((item, count) in outputMaterials) {
            put(item.id, count)
        }
    }

    return RecipeSave(id, name, machineName, _inputMaterials, _outputMaterials, primaryOutput.id)
}