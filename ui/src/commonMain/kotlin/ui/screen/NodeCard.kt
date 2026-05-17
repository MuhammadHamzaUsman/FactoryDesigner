package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.factory.Item
import org.example.graph.node.SinkNode
import org.example.graph.node.SourceNode
import org.example.graph.node.TransformationNode
import ui.composables.LabelTextField
import ui.logic.GraphEditorLogic
import ui.model.UiNode
import ui.modifier.drag
import util.toDoubleRoundedStringOrEmpty


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NodeCard(
    uiNode: UiNode,
    nodeName: String,
    inputMaterialCount: LinkedHashMap<Item, Double?>,
    outputMaterialCount: LinkedHashMap<Item, Double?>,
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier,
    containerCords: LayoutCoordinates?
){
    val node = controller.getNode(uiNode.id)
    val state by controller.state.collectAsState()
    val machineCount = state.machineCount[uiNode.id]
    var textFieldValue by remember { mutableStateOf("") }

    LaunchedEffect(machineCount){
        if(machineCount != null) {
            textFieldValue = machineCount.toDoubleRoundedStringOrEmpty(2)
        }
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .graphicsLayer {
                translationX = uiNode.position.x
                translationY = uiNode.position.y
            }
    ) {
        if(node !is SourceNode) {
            Card(
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colorScheme.surfaceBright,
                shape = RoundedCornerShape(16.dp),
            ) {
                ItemColumn(
                    uiNode = uiNode,
                    isInput = true,
                    controller = controller,
                    containerCords = containerCords,
                    items = inputMaterialCount,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        Card(
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .width(300.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(
                            if(node is TransformationNode) 0.dp else 4.dp
                        )
                        .drag(
                            key = uiNode.id,
                            controller = controller
                        ) {
                            updateNodePosition(uiNode.id, it)
                        }
                ) {
                    Text(
                        text = nodeName,
                        style = MaterialTheme.typography.titleLarge,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove Node",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.CenterEnd)
                            .clickable{
                                controller.removeNode(uiNode)
                            }
                    )
                }

                if (node is TransformationNode) {
                    Spacer(
                        modifier = Modifier
                            .size(250.dp, 1.dp)
                            .background(color = MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                            .padding(vertical = 8.dp)
                    )

                    LabelTextField(
                        label = "Machine Count",
                        value = textFieldValue,
                        spacing = 8.dp,
                        onValueChange = { textFieldValue = it },
                    ) {
                        if (it.isNotEmpty() && it.matches(Regex("^\\d*\\.?\\d*$"))) {
                            controller.setMachineCount(uiNode.id, it)
                        }
                    }
                }
            }
        }
        if(node !is SinkNode) {
            Card(
                elevation = 4.dp,
                backgroundColor = MaterialTheme.colorScheme.surfaceDim,
                shape = RoundedCornerShape(16.dp),
            ) {
                ItemColumn(
                    uiNode = uiNode,
                    isInput = false,
                    controller = controller,
                    containerCords = containerCords,
                    items = outputMaterialCount,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}