package ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import org.example.factory.Item
import ui.composables.LabelTextField
import ui.logic.GraphEditorLogic
import ui.model.UiNode
import util.screenToWorld
import util.toDoubleRoundedStringOrEmpty

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemColumn(
    uiNode: UiNode,
    isInput: Boolean,
    controller: GraphEditorLogic,
    containerCords: LayoutCoordinates?,
    items: LinkedHashMap<Item, Double?>,
    modifier: Modifier = Modifier
){
    var textField by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for ((item, count) in items) {

            LaunchedEffect(count){
                val formatted = count.toDoubleRoundedStringOrEmpty(2)

                if(textField != formatted){
                    textField = formatted
                }
            }

            var nodePos by remember(item, uiNode.position) { mutableStateOf(Offset(0f, 0f)) }

            LabelTextField(
                label = item.name,
                value = textField,
                spacing = 8.dp,
                onDone = {
                    if (it.isNotEmpty() && it.matches(Regex("^\\d*\\.?\\d*$"))) {
                        controller.setItemCount(uiNode.id, item, it, isInput)
                    }
                },
                onValueChange = { textField = it },
                modifier = Modifier
                    .onGloballyPositioned { childCords ->
                        if (containerCords != null && childCords.isAttached) {
                            val localCenter = Offset(childCords.size.width / 2f, childCords.size.height / 2f)
                            val sceneOffset = containerCords.localPositionOf(childCords, localCenter)

                            nodePos = sceneOffset.screenToWorld(controller.state.value.camera)
                        }
                    }
                    .combinedClickable(
                        onDoubleClick = {
                            controller.setDisplayEdge(uiNode.id, item, isInput, nodePos)
                        }
                    ) {
                        if(isInput){
                            controller.handleInputConnectorClick(uiNode.id, item, nodePos)
                        }
                        else{
                            controller.handleOutputConnectorClick(uiNode.id, item, nodePos)
                        }
                    }
                    .width(150.dp)
            )
        }
    }
}