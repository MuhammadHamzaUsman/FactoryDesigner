package ui.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.screen.MenuBar
import ui.screen.PopUpMessage
import ui.state.AppState

@Composable
fun GraphScreen(
    appState: AppState,
    modifier: Modifier = Modifier
){
    val controller by appState.controller.collectAsState()

    val errorMessage by appState.errorMessage.collectAsState()
    val message by appState.message.collectAsState()

    Box(
        modifier = modifier
    ) {
        Column{
            MenuBar(appState)

            controller?.let {
                NodeEditor(
                    controller!!,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        message?.let {
            PopUpMessage(
                message!!,
                false,
                modifier = Modifier.align(Alignment.Center)
            ){
                appState.okMessage()
            }
        }

        errorMessage?.let {
            PopUpMessage(
                errorMessage!!,
                true,
                modifier = Modifier.align(Alignment.Center)
            ){
                appState.okError()
            }
        }
    }
}