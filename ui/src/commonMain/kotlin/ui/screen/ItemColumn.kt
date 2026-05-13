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
import util.round
import util.screenToWorld

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemColumn(
    uiNode: UiNode,
    isInput: Boolean,
    controller: GraphEditorLogic,
    containerCords: LayoutCoordinates?,
    items: LinkedHashMap<Item, Double?>,
    modifier: Modifier = Modifier,
    onValueChange: (uiNode: UiNode, item: Item, newValue: Double?, newPositionCenter: Offset) -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for ((item, count) in items) {

            var nodePos by remember(item, uiNode.position) { mutableStateOf(Offset(0f, 0f)) }

            LabelTextField(
                label = item.name,
                value = count?.round(2)?.toString() ?: "",
                spacing = 8.dp,
                onDone = {},
                onValueChange = { onValueChange(uiNode, item, it.toDoubleOrNull(), nodePos) },
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