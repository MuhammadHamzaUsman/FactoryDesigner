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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.factory.Item
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
    nodeCount: String,
    inputMaterialCount: LinkedHashMap<Item, Double?>,
    outputMaterialCount: LinkedHashMap<Item, Double?>,
    onMachineCountValueChange: (uiNode: UiNode, newValue: Double?, newPositionCenter: Offset) -> Unit,
    onInputMaterialCountChange: (uiNode: UiNode, item: Item, newValue: Double?, newPositionCenter: Offset) -> Unit,
    onOutputMaterialCountChange: (uiNode: UiNode, item: Item, newValue: Double?, newPositionCenter: Offset) -> Unit,
    controller: GraphEditorLogic,
    modifier: Modifier = Modifier,
    containerCords: LayoutCoordinates?
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .graphicsLayer {
                translationX = uiNode.position.x
                translationY = uiNode.position.y
            }
    ) {
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
                onValueChange = onInputMaterialCountChange,
                modifier = Modifier.padding(8.dp)
            )
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
                        .padding(bottom = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
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
                            .align(Alignment.BottomEnd)
                            .clickable{
                                controller.removeNode(uiNode)
                            }
                    )
                }

                Spacer(
                    modifier = Modifier
                        .size(250.dp, 1.dp)
                        .background(color = MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                        .padding(vertical = 8.dp)
                )

                LabelTextField(
                    label = "Machine Count",
                    value = nodeCount.toDoubleRoundedStringOrEmpty(2),
                    spacing = 8.dp,
                    onValueChange = { onMachineCountValueChange(uiNode, it.toDoubleOrNull(), uiNode.position) }
                )
            }
        }

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
                onValueChange = onOutputMaterialCountChange,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}