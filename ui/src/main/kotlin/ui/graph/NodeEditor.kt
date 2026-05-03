package ui.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.example.graph.Graph

@Composable
fun GraphScreen(
    modifier: Modifier = Modifier
){
    Canvas(
        modifier = modifier.fillMaxSize(),
        contentDescription = ""
    ){
        drawRect(
            color = Color.Red,
            topLeft = Offset(-10f, -10f),
            size = Size(100f, 100f)
        )
    }
}