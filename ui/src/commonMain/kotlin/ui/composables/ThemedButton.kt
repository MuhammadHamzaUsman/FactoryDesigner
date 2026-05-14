package ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ui.modifier.clickableWithOffset
import ui.screen.ThemedText

@Composable
fun ThemedButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (Offset) -> Unit
){
    ThemedText(
        text = text,
        align = TextAlign.Center,
        modifier = modifier
            .width(240.dp)
            .border(
                color = MaterialTheme.colorScheme.outline,
                width = 2.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(4.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(4.dp)
            )
            .clickableWithOffset(onClick)
    )
}