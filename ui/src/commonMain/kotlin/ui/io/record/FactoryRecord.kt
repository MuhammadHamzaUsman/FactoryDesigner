package ui.io.record

import kotlinx.serialization.Serializable

@Serializable
data class FactoryRecord (
    val items: List<ItemRecord>,
    val recipes: List<RecipeRecord>
)