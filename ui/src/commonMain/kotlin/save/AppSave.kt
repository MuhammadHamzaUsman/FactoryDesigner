package save

import kotlinx.serialization.Serializable
import save.dto.CountersSave
import save.dto.GraphEditorLayoutStateSave
import save.dto.GraphSave
import save.dto.ItemAndRecipeStateSave

@Serializable
data class AppSave(
    val countersSave: CountersSave,
    val graphSave: GraphSave,
    val graphEditorLayoutStateSave: GraphEditorLayoutStateSave,
    val itemAndRecipeStateSave: ItemAndRecipeStateSave
)