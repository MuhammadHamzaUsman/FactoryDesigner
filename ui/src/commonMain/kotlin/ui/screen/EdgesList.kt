package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import itemAndRecipe
import org.example.data.ItemAndRecipeState
import org.example.factory.Item
import org.example.graph.Edge
import org.example.graph.Graph
import org.example.graph.node.SplitterNode
import org.example.graph.node.TransformationNode
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.logic.GraphEditorLogic
import ui.model.Camera
import ui.state.GraphEditorLayoutState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EdgesList(
    edgesList: List<Edge>,
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier
){
    var textField by remember { mutableStateOf("") }

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
            .width(330.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        stickyHeader {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    )
            ){
                Text(
                    text = "Edges List",
                    style = MaterialTheme.typography.titleLarge,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                )

                IconButton(
                    onClick = { controller.resetEdgeListUpdate() },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close Edges List",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            }
        }

        items(edgesList){edge ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(bottom = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                NodeName(
                    edge.source.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .width(2.dp)
                        .background(color = MaterialTheme.colorScheme.outlineVariant)
                        .align(Alignment.CenterVertically)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp)
                ){
                    Text(
                        text = edge.item.name,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        lineHeight = 16.sp,
                    )

                    TextField(
                        value = textField,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (textField.isNotEmpty() && textField.matches(Regex("^\\d*\\.?\\d*$"))) {
                                    controller.updateEdgeWeight(edge.id, textField.toFloatOrNull() ?: return@KeyboardActions)
                                }
                                else{
                                    textField = ""
                                }
                            }
                        ),
                        onValueChange = {
                            textField = it
                        }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .width(2.dp)
                        .background(color = MaterialTheme.colorScheme.outlineVariant)
                        .align(Alignment.CenterVertically)
                )

                NodeName(
                    edge.destination.name,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                )

                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Close Edges List",
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                        .align(Alignment.CenterVertically)
                        .clickable{
                            controller.deleteEdge(edge.id)
                        }
                )
            }
        }
    }
}

@Composable
private fun NodeName(
    name: Array<String>,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(name.isNotEmpty()) {
            Text(
                text = name[0],
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 16.sp
            )

            for (i in 1..name.lastIndex) {
                Text(
                    text = name[i],
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun EdgesListPreview(){
    AppTheme {
        EdgesList(
            edgesList = listOf(
                Edge(TransformationNode(itemAndRecipe.recipes[0L]!!), Item("Scrap"), SplitterNode(Item("Scrap")))
            ),
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
            ),
        )
    }
}