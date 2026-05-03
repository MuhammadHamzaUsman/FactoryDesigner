package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.factory.Item
import org.example.graph.node.Node
import org.example.graph.node.TransformationNode
import ui.iron
import ui.model.UiNode
import ui.scrap
import ui.theme.bodyFontFamily
import util.toDoubleRoundedStringOrEmpty
import kotlin.collections.getValue
import kotlin.collections.setValue

@Composable
fun NodeCard(uiNode: UiNode, node: Node, modifier: Modifier = Modifier){
    val recipe = (node as TransformationNode).recipe
    var machineCount by remember { mutableStateOf("") }

    val inputMaterialCount = remember {
        mutableStateMapOf<Item, Double>().apply {
            this[iron] = 10.0
        }
    }

    val outputMaterialCount = remember {
        mutableStateMapOf<Item, Double>().apply {
            this[scrap] = 20.0
        }
    }

    val inputMaterial = recipe.inputMaterials.keys.toList()
    val outputMaterial = recipe.outputMaterials.keys.toList()

    Card(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
        ) {

            Text(
                text = recipe.machineName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )

            Spacer(
                modifier = Modifier
                    .size(200.dp, 1.dp)
                    .background(color = MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )

            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .align(Alignment.Start)
            ) {

                Text(
                    text = "Count: ",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                BasicTextField(
                    value = machineCount.toDoubleRoundedStringOrEmpty(2),
                    onValueChange = {
                        machineCount = it
                    },
                    textStyle = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ItemColumn(
                    items = inputMaterial,
                    itemValueMap = inputMaterialCount,
                    modifier = Modifier.padding(end = 6.dp)
                ){ item, double -> "$double - ${item.name}" }

                ItemColumn(
                    items = outputMaterial,
                    itemValueMap = outputMaterialCount,
                    modifier = Modifier.padding(start = 6.dp)
                ) { item, double -> "${item.name} - $double" }
            }
        }
    }
}

@Composable
private fun ItemColumn(
    items: List<Item>,
    itemValueMap: Map<Item, Double>,
    modifier: Modifier = Modifier,
    toString: (Item, Double) -> String
){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for (item in items) {
            Text(
                text = toString(item, itemValueMap[item] ?: 0.0),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
        }
    }
}