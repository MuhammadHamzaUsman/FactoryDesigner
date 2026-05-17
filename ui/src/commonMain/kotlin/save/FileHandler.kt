package save

import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

fun showSaveDialog(defaultFileName: String = "app_save.json"): String? {
    val dialog = FileDialog(null as Frame?, "Save File", FileDialog.SAVE)
    dialog.file = defaultFileName
    dialog.isVisible = true

    val file = dialog.file ?: return null
    val dir = dialog.directory ?: return null

    return dir + file
}

fun selectAndReadJsonFile(): Pair<String, String>? {
    val dialog = FileDialog(null as Frame?, "Select JSON File", FileDialog.LOAD)

    dialog.filenameFilter = FilenameFilter { _, name ->
        name.lowercase().endsWith(".json")
    }

    dialog.isVisible = true

    val directory = dialog.directory
    val file = dialog.file

    if (directory != null && file != null) {
        val selectedFile = File(directory, file)
        return selectedFile.readText(Charsets.UTF_8) to selectedFile.absolutePath
    }
    return null
}