package ui.io

import kotlinx.serialization.json.Json
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe
import ui.io.record.FactoryRecord
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter

fun selectAndReadJsonFile(): String? {
    val dialog = FileDialog(null as Frame?, "Select JSON File", FileDialog.LOAD)

    dialog.filenameFilter = FilenameFilter { _, name ->
        name.lowercase().endsWith(".json")
    }

    dialog.isVisible = true

    val directory = dialog.directory
    val file = dialog.file

    if (directory != null && file != null) {
        val selectedFile = File(directory, file)
        return selectedFile.readText(Charsets.UTF_8)
    }
    return null
}

// item.json should contain an array of items where item's index is item's id

fun loadItemAndRecipe(jsonString: String): ItemAndRecipeState {
    val factoryRecord: FactoryRecord = Json.decodeFromString(jsonString)
    var name: String

    Item.resetCounter()
    Recipe.resetCounter()

    var item: Item
    val items = LinkedHashMap<Long, Item>()
    val itemRecords = mutableMapOf<String, Item>()

    for(itemRecord in factoryRecord.items){
        name = itemRecord.name.trim()

        if(name.isBlank()) throw IllegalArgumentException("Item name can not be blank: Blank name [$name]")
        if(itemRecords.contains(name)) throw IllegalArgumentException("Item name should be unique: Double name [$name]")

        item = Item(name)
        items[item.id] = item
        itemRecords[name] = item
    }

    var recipe: Recipe
    val recipes = LinkedHashMap<Long, Recipe>()
    val recipeRecords = mutableSetOf<String>()

    var recipeName: String
    var primaryOutput: Item
    var inputMaterials: LinkedHashMap<Item, Double>
    var outputMaterials: LinkedHashMap<Item, Double>

    for(recipeRecord in factoryRecord.recipes){
        recipeName = recipeRecord.name.trim()

        if(recipeName.isBlank()) throw IllegalArgumentException("Recipe name can not be blank: Blank name [$recipeName]")
        if(recipeRecords.contains(recipeName)) throw IllegalArgumentException("Recipe name should be unique: Double name [$recipeName]")

        recipeRecords.add(recipeName)

        inputMaterials = LinkedHashMap()

        for ((itemName, count) in recipeRecord.inputMaterials) {
            name = itemName.trim()
            if(recipeName.isBlank()) throw IllegalArgumentException("Recipe [$recipeName] inputMaterial name [$name] can not be blank: Blank name [$recipeName]")
            if(count < 0) throw IllegalArgumentException("Recipe [$recipeName] can not contain inputMaterial [$name] with negative value [$count]")

            item = itemRecords[name] ?: throw IllegalArgumentException("Recipe [$recipeName] contains item in inputMaterials not defined in \"items\": Undefined item [$name]")
            inputMaterials[item] = count
        }

        outputMaterials = LinkedHashMap()

        for ((itemName, count) in recipeRecord.outputMaterials) {
            name = itemName.trim()
            if(recipeName.isBlank()) throw IllegalArgumentException("Recipe [$recipeName] outputMaterial name [$name] can not be blank: Blank name [$recipeName]")
            if(count < 0) throw IllegalArgumentException("Recipe [$recipeName] can not contain outputMaterial [$name] with negative value [$count]")

            item = itemRecords[name] ?: throw IllegalArgumentException("Recipe [$recipeName] contains item in outputMaterials not defined in \"items\": Undefined item [$name]")
            outputMaterials[item] = count
        }

        name = recipeRecord.primaryOutput.trim()
        primaryOutput = itemRecords[name] ?: throw IllegalArgumentException("Recipe [$recipeName] primaryOutput not defined in \"items\": Undefined item [$name]")

        recipe = Recipe(
            recipeName,
            recipeRecord.machineName,
            inputMaterials,
            outputMaterials,
            primaryOutput
        )

        recipes[recipe.id] = recipe
    }

    return ItemAndRecipeState(items, recipes).also { println(it) }
}