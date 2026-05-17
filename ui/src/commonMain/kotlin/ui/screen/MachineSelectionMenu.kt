package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Graph
import org.example.graph.node.NodeType
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.composables.ThemedButton
import ui.composables.ThemedRadioButton
import ui.logic.GraphEditorLogic
import ui.model.Camera
import ui.model.FilterOption
import ui.state.GraphEditorLayoutState

@Composable
fun MachineSelectionMenu(
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
) {
    val searchStr by controller.searchText.collectAsState()
    val selectedFilter by controller.filterOption.collectAsState()
    val filteredRecipes by controller.filteredRecipe.collectAsState(emptyList())
    val filteredItems by controller.filteredItem.collectAsState(emptyList())
    val itemAndRecipeState by controller.itemAndRecipeState.collectAsState()

    Column(
        modifier = modifier
            .size(500.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        TextField(
            value = searchStr,
            onValueChange = controller::updateSearchText,
            textStyle = MaterialTheme.typography.bodyLarge.copy(lineHeight = 16.sp),
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp)),
        )

        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp)
            ){
                for (i in 0..<FilterOption.entries.size step 4) {
                    Row{
                        for (option in FilterOption.entries.subList(
                            i,
                            (i + 4).coerceAtMost(FilterOption.entries.size)
                        )) {
                            ThemedRadioButton(
                                label = option.text,
                                selected = selectedFilter == option,
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp)),
                            ) {
                                if (selectedFilter == option) {
                                    controller.updateFilterOption(null)
                                } else {
                                    controller.updateFilterOption(option)
                                }
                            }
                        }
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            when(selectedFilter){
                FilterOption.RECIPE, FilterOption.INPUT_MATERIAL, FilterOption.OUTPUT_MATERIAL -> {
                    items(filteredRecipes, key = Recipe::id) {
                        RecipeCard(it, controller)
                    }
                }

                FilterOption.Source -> {
                    items(filteredItems, key = Item::id) {item ->
                        ThemedButton(item.name) { offset ->
                            controller.addNode(item, offset, NodeType.SOURCE)
                        }
                    }
                }

                FilterOption.Sink -> {
                    items(filteredItems, key = Item::id) {item ->
                        ThemedButton(item.name) { offset ->
                            controller.addNode(item, offset, NodeType.SINK)
                        }
                    }
                }

                FilterOption.MERGER -> {
                    items(filteredItems, key = Item::id) {item ->
                        ThemedButton(item.name) { offset ->
                            controller.addNode(item, offset, NodeType.MERGER)
                        }
                    }
                }

                FilterOption.SPLITTER -> {
                    items(filteredItems, key = Item::id) {item ->
                        ThemedButton(item.name) { offset ->
                            controller.addNode(item, offset, NodeType.SPLITTER)
                        }
                    }
                }

                else -> {}
            }
        }
    }
}

@Preview
@Composable
fun PreviewMachineSelectionMenu(){
    AppTheme {
        MachineSelectionMenu(
            controller = GraphEditorLogic(
                Graph(),
                ItemAndRecipeState(LinkedHashMap(), LinkedHashMap()),
                GraphEditorLayoutState(
                    mutableStateMapOf(),
                    mutableStateMapOf(),
                    Camera(Offset.Zero , 1f),
                    null,
                    mutableStateMapOf()
                )
            )
        )
    }
}