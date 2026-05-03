package ui.model.node

import androidx.compose.ui.geometry.Offset

class NodePositionMap {
    private val positionMap = mutableMapOf<Long, Offset>()

    fun set(nodeId: Long, position: Offset){
        positionMap[nodeId] = position
    }

    fun get(nodeId: Long) = positionMap[nodeId] ?: Offset.Zero
}