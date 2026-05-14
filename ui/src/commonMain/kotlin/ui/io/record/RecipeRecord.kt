package ui.io.record

import kotlinx.serialization.Serializable

@Serializable
data class RecipeRecord(
    val name: String = "",
    val machineName: String = "",
    val inputMaterials: Map<String, Double> = emptyMap(),
    val outputMaterials: Map<String, Double> = emptyMap(),
    val primaryOutput: String = ""
)