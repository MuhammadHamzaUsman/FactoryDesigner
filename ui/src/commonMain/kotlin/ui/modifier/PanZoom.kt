package ui.modifier

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import ui.logic.GraphEditorLogic

fun Modifier.panZoom(controller: GraphEditorLogic) = pointerInput(1){
    awaitPointerEventScope {
        var event: PointerEvent
        while (true){
            event = awaitPointerEvent(PointerEventPass.Main)

            when(event.type) {
                PointerEventType.Scroll -> controller.updateZoom(
                    scrollDelta = event.changes.first().scrollDelta.y,
                    cursorPositon = event.changes.first().position
                )
            }
        }
    }
}.pointerInput(2){
    detectDragGestures { _, dragAmount -> controller.updateCameraPosition(dragAmount) }
}