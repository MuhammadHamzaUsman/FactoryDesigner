package ui.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import ui.logic.GraphEditorLogic

fun Modifier.panZoom(controller: GraphEditorLogic) = pointerInput(Unit){
    awaitPointerEventScope {
        var event: PointerEvent
        while (true){
            event = awaitPointerEvent(PointerEventPass.Main)

            when(event.type) {
                PointerEventType.Scroll -> controller.updateZoom(
                    scrollDelta = event.changes.first().scrollDelta.y,
                    cursor = event.changes.first().position
                )
            }
        }
    }
}.drag(controller = controller){ updateCameraPosition(it) }
