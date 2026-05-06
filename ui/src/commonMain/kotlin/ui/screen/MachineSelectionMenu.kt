package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.compose.AppTheme
import inputMaterial
import ironIngot
import nameFromId
import org.example.factory.Machine
import org.example.factory.Recipe
import org.jetbrains.compose.ui.tooling.preview.Preview
import outputMaterial
import ui.composables.FixedLabelButton
import ui.composables.ThemedRadioButton
import ui.model.FilterOption

@Composable
fun MachineSelectionMenu(
    machine: Machine,
    modifier: Modifier = Modifier
) {
    var searchStr by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<FilterOption?>(null) }

    Column(
        modifier = modifier
            .size(500.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
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
            onValueChange = {str -> searchStr = str},
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
        )

        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Row {
                for (option in FilterOption.entries) {
                    ThemedRadioButton(
                        label = option.text,
                        selected = selectedFilter == option,
                        spacing = 0.dp,
                        onClick = {
                            if (selectedFilter == option) {
                                selectedFilter = null
                            } else {
                                selectedFilter = option
                            }
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(start = 14.dp)
            ) {
                FixedLabelButton("Splitter")
                FixedLabelButton("Merger")
            }
        }
    }
}


@Preview
@Composable
fun PreviewMachineSelectionMenu(){
    AppTheme {
        MachineSelectionMenu(
            Machine(
                LinkedHashSet<Recipe>().apply {
                    Recipe(
                        "Something",
                        nameFromId(0L),
                        inputMaterial(0L),
                        outputMaterial(0L),
                        ironIngot
                    )
                }
            )
        )
    }
}