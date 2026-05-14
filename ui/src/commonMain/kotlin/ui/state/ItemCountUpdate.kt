package ui.state

import org.example.factory.Item

data class ItemCountUpdate(
    val nodeId: Long,
    val item: Item,
    val itemCount: String,
    val isInput: Boolean
)
