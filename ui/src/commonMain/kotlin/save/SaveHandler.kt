package save

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ui.logic.GraphEditorLogic
import java.io.File

object SaveHandler {
    val JSON = Json {
        prettyPrint = true
    }

    fun saveToFile(save: AppSave): String?{
        val filePath = showSaveDialog("save.json") ?: return null
        val file = File(filePath)
        val json = JSON.encodeToString<AppSave>(save)

        file.writeText(json)

        return filePath
    }

    fun saveToFile(save: AppSave, path: String){
        val file = File(path)
        val json = JSON.encodeToString<AppSave>(save)

        file.writeText(json)
    }

    fun readFromFile(): Pair<GraphEditorLogic, String>? {
        val (json, path) = selectAndReadJsonFile() ?: return null
        val save = JSON.decodeFromString<AppSave>(json)
        val itemsAndRecipeState = save.itemAndRecipeStateSave.toItemAndRecipeState()
        val graph = save.graphSave.toGraph(itemsAndRecipeState.items, itemsAndRecipeState.recipes)
        val graphEditorLayoutState = save.graphEditorLayoutStateSave.toGraphEditorLayoutState()
        save.countersSave.updateCounters()

        return GraphEditorLogic(
            graph,
            itemsAndRecipeState,
            graphEditorLayoutState
        ) to path
    }
}