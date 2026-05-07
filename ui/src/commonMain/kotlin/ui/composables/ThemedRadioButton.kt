package ui.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemedRadioButton(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        fontSize = 18.sp,
        color = if(selected) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .background(color = if(selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceContainerHigh)
    )
}

@Preview
@Composable
fun ThemedRadioButtonPreview(){
    ThemedRadioButton(
        "Filter 1",
        true
    ){

    }
}