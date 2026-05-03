package org.example.graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Graph {
    public final List<Node> nodes;
    public final List<Edge> edges ;

    public Graph() {
        this(new ArrayList<>(), new ArrayList<>());
    }

    public void sortNodes(){
        nodes.sort(Comparator.comparing(n -> n.id));
    }

    public List<Edge> collectOutputEdgesOfNode(Node node){
        List<Edge> outputEdges = new ArrayList<>();

        for (Edge edge: edges) {
            if(edge.source != null && edge.source.equals(node)){
                outputEdges.add(edge);
            }
        }

        return outputEdges;
    }

    public List<Edge> collectInputEdgesOfNode(Node node){
        List<Edge> inputEdges = new ArrayList<>();

        for (Edge edge: edges) {
            if(edge.destination != null && edge.destination.equals(node)){
                inputEdges.add(edge);
            }
        }

        return inputEdges;
    }

    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }
}
