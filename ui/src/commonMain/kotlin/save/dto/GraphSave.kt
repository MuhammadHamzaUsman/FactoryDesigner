package save.dto

import kotlinx.serialization.Serializable
import org.example.factory.Item
import org.example.factory.Recipe
import org.example.graph.Edge
import org.example.graph.Graph

@Serializable
data class GraphSave(
    val edges: List<EdgeSave>,
    val nodes: List<NodeSave>
){
    fun toGraph(itemsMap: Map<Long, Item>, recipesMap: Map<Long, Recipe>): Graph {
        val graph = Graph()
        var edge: Edge

        for (save in nodes) {
            graph.addNode(save.toNode(itemsMap, recipesMap))
        }

        for (save in edges) {
            edge = save.toEdge(
                graph.getNode(save.source) ?: throw IllegalStateException("Source node not found for edgeId[${save.id}] during deserialization"),
                itemsMap,
                graph.getNode(save.destination) ?: throw IllegalStateException("Destination node not found for edgeId[${save.id}] during deserialization")
            )
            graph.addEdge(edge)
        }

        return graph
    }
}

fun Graph.toSave() = GraphSave(
    edgesIdMap.values.map { it.toSave() },
    nodeIdMap.values.map { it.toSave() }
)