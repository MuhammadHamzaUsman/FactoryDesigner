package ui.composables

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ThemedRadioButton(
    label: String,
    selected: Boolean,
    spacing: Dp = 4.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier
    ){
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                unselectedColor = MaterialTheme.colorScheme.tertiaryContainer,
                selectedColor = MaterialTheme.colorScheme.tertiary
            )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp,
        )
    }
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