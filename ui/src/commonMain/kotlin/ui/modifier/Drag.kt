package ui.modifier

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import ui.logic.GraphEditorLogic

fun Modifier.drag(
    key: Any? = null,
    controller: GraphEditorLogic,
    onStart: () -> Unit = {},
    onEnd: () -> Unit = {},
    onDrag: GraphEditorLogic.(delta: Offset) -> Unit
) = pointerInput(key ?: Unit){

    detectDragGestures(
        onDragStart = { onStart() },
        onDragEnd = onEnd
    ){ change, dragAmount ->
        change.consume()
        controller.onDrag(dragAmount)
    }
}