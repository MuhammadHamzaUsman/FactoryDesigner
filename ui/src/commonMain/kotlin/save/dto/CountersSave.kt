package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Edge
import org.example.graph.node.Node

@Serializable
data class CountersSave(
    val itemCounter: Long,
    val recipeCounter: Long,
    val nodeCounter: Long,
    val edgeCounter: Long,
){
    fun updateCounters(){
        Item.setCounter(itemCounter)
        Recipe.setCounter(recipeCounter)
        Node.setCounter(nodeCounter)
        Edge.setCounter(edgeCounter)
    }

    companion object{
        fun createFromCurrentValues() = CountersSave(
            Item.getCounter(),
            Recipe.getCounter(),
            Node.getCounter(),
            Edge.getCounter()
        )
    }
}