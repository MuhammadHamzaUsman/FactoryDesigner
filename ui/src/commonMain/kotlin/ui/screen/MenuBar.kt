package ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.state.AppState

@Composable
fun MenuBar(
    appState: AppState,
    modifier: Modifier = Modifier
){
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .width(230.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .width(75.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(
                        0.dp,
                        0.dp,
                        if(expanded) 8.dp else 0.dp,
                        0.dp
                    )
                )
                .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(
                        0.dp,
                        0.dp,
                        if(expanded) 8.dp else 0.dp,
                        0.dp
                    )
                )
                .padding(4.dp)
        ) {
            ThemedText("File"){ expanded = !expanded }

            if(expanded){
                ThemedText("New"){ appState.createNewGraph() }
                ThemedText("Open"){ appState.openGraph() }
                ThemedText("Save") { appState.saveGraph() }
                ThemedText("Save As"){ appState.saveAsGraph() }
            }
        }

        ThemedText(
            text = "Item And Recipe",
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 0.dp)
                )
                .padding(start = 0.dp, end = 6.dp, top = 0.dp, bottom = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(0.dp, 0.dp, 8.dp, 0.dp)
                )
                .padding(4.dp)
        ){ appState.addItemAndRecipe() }
    }
}

@Composable
private fun ThemedText(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp,
        color = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick)
    )
}