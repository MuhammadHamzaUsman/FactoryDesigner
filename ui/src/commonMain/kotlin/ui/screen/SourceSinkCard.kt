package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.example.factory.Item
import ui.logic.GraphEditorLogic
import ui.modifier.clickableWithOffset

@Composable
fun SourceSinkCard(
    isSource: Boolean,
    item: Item,
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
            .padding(4.dp)
            .clickableWithOffset{
                controller.addNode(item, it, isSource)
            }
    ){
        ThemedText(
            text = item.name,
            align = TextAlign.Center
        )
    }
}