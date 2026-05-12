package ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.factory.Recipe
import org.example.graph.node.NodeType
import ui.logic.GraphEditorLogic
import ui.modifier.clickableWithOffset

@Composable
fun RecipeCard(
    recipe: Recipe,
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
){
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .width(240.dp)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 2.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableWithOffset(
                onClick = { controller.addNode(recipe, it) }
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            ThemedText(
                text = recipe.name,
                align = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(1.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.outline)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                ItemColumn(
                    itemTitle = "Input Materials",
                    items = recipe.inputMaterials.keys.map { it.name }.toList(),
                    modifier = Modifier.weight(1f)
                )
                ItemColumn(
                    itemTitle = "Output Materials",
                    items = recipe.outputMaterials.keys.map { it.name }.toList(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ItemColumn(
    itemTitle: String,
    items: List<String>,
    modifier: Modifier = Modifier
){
    Column(modifier = modifier) {
        ThemedText(
            text = itemTitle,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        items.forEach {
            ThemedText(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ThemedText(
    text: String,
    align: TextAlign = TextAlign.Left,
    style:  TextStyle = MaterialTheme.typography.labelLarge,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = style,
        lineHeight = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = align,
        modifier = modifier.fillMaxWidth()
    )
}