package save.dto

import kotlinx.serialization.Serializable
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe

@Serializable
data class ItemAndRecipeStateSave(
    val items: LinkedHashMap<Long, ItemSave>,
    val recipes: LinkedHashMap<Long, RecipeSave>
){
    fun toItemAndRecipeState(): ItemAndRecipeState{
        val _items = LinkedHashMap<Long, Item>().apply {
            for (save in items.values) {
                put(save.id, save.toItem())
            }
        }

        val _recipes = LinkedHashMap<Long, Recipe>().apply {
            for (save in recipes.values) {
                put(save.id, save.toRecipe(_items))
            }
        }

        return ItemAndRecipeState(_items, _recipes)
    }
}

fun ItemAndRecipeState.toSave() = ItemAndRecipeStateSave(
    LinkedHashMap<Long, ItemSave>().apply {
        for ((id, item) in items) {
            put(id, item.toSave())
        }
    },
    LinkedHashMap<Long, RecipeSave>().apply {
        for ((id, item) in recipes) {
            put(id, item.toSave())
        }
    }
)
