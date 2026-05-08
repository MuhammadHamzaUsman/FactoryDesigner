package ui.modifier

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clickableWithOffset(onClick: (Offset) -> Unit) = pointerInput(Unit){
    detectTapGestures(onTap = onClick)
}