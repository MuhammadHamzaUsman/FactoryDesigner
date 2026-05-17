package ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PopUpMessage(
    message: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .border(
                width = 2.dp,
                color = if(isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if(isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Error: $message",
            tint = if(isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 14.sp,
            color = if(isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
            softWrap = true,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(300.dp)
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            onClick = onClick,
            border = BorderStroke(
                width = 2.dp,
                color = if(isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface
            ),
            contentPadding = PaddingValues(0.dp)
        ){
            Text(
                text = "OK",
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if(isError) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurface,
                softWrap = true
            )
        }
    }
}