package ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LabelTextField(
    label: String,
    value: String,
    spacing: Dp,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    onLabelClick: () -> Unit = {},
    onDone: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .clickable(onClick = onLabelClick)
                .padding(end = spacing)
        )

        BasicTextField(
            enabled = enabled,
            value = value,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(
                onDone = { onDone(value) }
            ),
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.labelLarge,
            singleLine = true,
            modifier = Modifier
                .border(
                    color = MaterialTheme.colorScheme.outline,
                    width = 2.dp,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(spacing)
                .fillMaxWidth()
        )
    }
}