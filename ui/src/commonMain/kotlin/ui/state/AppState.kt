package ui.state

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.SerializationException
import org.example.data.ItemAndRecipeState
import org.example.graph.Graph
import save.SaveHandler
import ui.logic.GraphEditorLogic
import ui.model.Camera

class AppState(controller: GraphEditorLogic?){
    private var _controller = MutableStateFlow(controller)
    val controller = _controller.asStateFlow()

    private var _savePath = MutableStateFlow<String?>(null)
    val savePath = _savePath.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun updateController(controller: GraphEditorLogic){
        _controller.update { controller }
    }

    fun updatePath(path: String){
        _savePath.update { path }
    }

    fun generateError(message: String){
        _errorMessage.update { message }
        controller.value?.generateError()
    }

    fun okError() {
        _errorMessage.update { null }
        controller.value?.okError()
    }

    fun createNewGraph(){
        try {
            val savePath = savePath.value
            val controller = controller.value

            if(controller != null && savePath != null){
                SaveHandler.saveToFile(controller.generateAppSave(), savePath)
            }

            val newController = GraphEditorLogic(
                Graph(),
                ItemAndRecipeState(LinkedHashMap(), LinkedHashMap()),
                GraphEditorLayoutState(
                    mutableStateMapOf(),
                    mutableStateMapOf(),
                    Camera(Offset.Zero, 1f),
                    null,
                    mutableStateMapOf()
                )
            )

            val path = SaveHandler.saveToFile(newController.generateAppSave())

            if(path != null){
                updateController(newController)
                updatePath(path)
            }
        }
        catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception) {
        when(e){
            is IllegalStateException, is NullPointerException, is IllegalArgumentException -> generateError(e.message ?: "Some Error")
            else -> throw e
        }
    }

    fun openGraph(){
        try {
            val result = SaveHandler.readFromFile()

            if(result == null){
                generateError("Could not Load")
                return
            }

            updateController(result.first)
            updatePath(result.second)
            okError()
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun saveGraph(){
        try {
            val path = savePath.value
            val controller = controller.value

            if(path != null && controller != null) {
                val appSave = controller.generateAppSave()
                SaveHandler.saveToFile(appSave, path)
                _message.update { "Save Successful" }
            }
        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun saveAsGraph(){
        try {
            val appSave = controller.value?.generateAppSave()

            if(appSave == null){
                generateError("No file to save.")
                return
            }

            val path = SaveHandler.saveToFile(appSave)

            if(path != null){
                updatePath(path)
                _message.update { "Save Successful" }
            }
            else {
                generateError("Save Unsuccessful")
            }

        } catch (e: Exception) {
            handleError(e)
        }
    }

    fun okMessage(){
        _message.update { null }
    }
}