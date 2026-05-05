package ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.dp
import org.example.factory.Item
import org.example.graph.node.NodeType
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.model.Camera
import ui.model.UiNode
import util.round
import util.screenToWorld

@Composable
fun ItemColumn(
    uiNode: UiNode,
    camera: Camera,
    items: LinkedHashMap<Item, Double?>,
    modifier: Modifier = Modifier,
    onValueChange: (uiNode: UiNode, item: Item, newValue: Double?, newPositionCenter: Offset) -> Unit
){
    var nodePos by remember { mutableStateOf(Offset(0f, 0f)) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        for ((item, count) in items) {
            LabelTextField(
                label = item.name,
                value = count?.round(2)?.toString() ?: "",
                spacing = 8.dp,
                onValueChange = { onValueChange(uiNode, item, it.toDoubleOrNull(), nodePos) },
                modifier = Modifier
                    .width(150.dp)
                    .onGloballyPositioned{ layoutCoordinates ->
                        val position = layoutCoordinates.positionInWindow()
                        val size = layoutCoordinates.size
                        nodePos = Offset(
                            position.x + size.width / 2,
                            position.y + size.height / 2
                    ).screenToWorld(camera)
                }
            )
        }
    }
}

@Preview
@Composable
private fun ItemColumnPreview(){
    ItemColumn(
        UiNode(
            0,
            Offset(0f, 0f),
            NodeType.TRANSFORMATION
        ),
        Camera(
            Offset(0f, 0f),
            1f
        ),
        LinkedHashMap<Item, Double?>().apply {
            this[Item("Iron")] = 10.0
            this[Item("Scrap")] = 30.0
            this[Item("Water")] = 50.0
            this[Item("Heat")] = 19.0
        },
        onValueChange = { node: UiNode, item: Item, d: Double?, offset: Offset -> }
    )
}