package org.example.graph;

import org.example.graph.node.Node;

import java.util.*;
import java.util.function.Consumer;

public class Graph {
    public final Map<Long, Edge> edgesIdMap;

    public final Map<Long, EdgeHolder> nodeEdgeMap;
    public final Map<Long, Node> nodeIdMap;

    public Graph() {
        this(List.of());
    }
    public Graph(Collection<Edge> edges) {
        this.edgesIdMap = new HashMap<>();
        this.nodeEdgeMap = new HashMap<>();
        this.nodeIdMap = new HashMap<>();

        for (Edge edge: edges) {
            addEdge(edge);
        }
    }

    public void addNode(Node node){
        nodeIdMap.put(node.id, node);
        nodeEdgeMap.putIfAbsent(node.id, new EdgeHolder());
    }

    public boolean addEdge(Edge edge){
        if(edgesIdMap.containsKey(edge.id)) return false;
        edgesIdMap.put(edge.id, edge);

        nodeIdMap.computeIfAbsent( edge.source.id, id -> {
            nodeEdgeMap.putIfAbsent(id, new EdgeHolder());
            return edge.source;
        });
        nodeEdgeMap.get(edge.source.id).outputEdges.add(edge);


        nodeIdMap.computeIfAbsent( edge.destination.id, id -> {
            nodeEdgeMap.putIfAbsent(id, new EdgeHolder());
            return edge.destination;
        });
        nodeEdgeMap.get(edge.destination.id).inputEdges.add(edge);

        return true;
    }

    public List<Node> removeEdge(long edgeId, boolean removeNodeIdNoEdge){
        if(!edgesIdMap.containsKey(edgeId)) return null;
        Edge edge = edgesIdMap.remove(edgeId);

        List<Node> removedNodes = new ArrayList<>();

        EdgeHolder sourceNodeEdges = nodeEdgeMap.get(edge.source.id);
        sourceNodeEdges.outputEdges.remove(edge);

        if(sourceNodeEdges.isEmpty() && removeNodeIdNoEdge){
            nodeEdgeMap.remove(edge.source.id);
            nodeIdMap.remove(edge.source.id);
            removedNodes.add(edge.source);
        }

        EdgeHolder destinationNodeEdges = nodeEdgeMap.get(edge.destination.id);
        destinationNodeEdges.inputEdges.remove(edge);

        if(destinationNodeEdges.isEmpty() && removeNodeIdNoEdge){
            nodeEdgeMap.remove(edge.destination.id);
            nodeIdMap.remove(edge.destination.id);
            removedNodes.add(edge.destination);
        }

        return removedNodes;
    }

    public Collection<Node> getNodes(){
        return nodeIdMap.values();
    }

    public Collection<Edge> getEdges(){
        return edgesIdMap.values();
    }

    public Node getNode(long nodeId){
        return nodeIdMap.get(nodeId);
    }

    public Edge getEdge(long edgeId){
        return edgesIdMap.get(edgeId);
    }

    public int nodeCount(){
        return nodeIdMap.size();
    }

    public int edgeCount(){
        return edgesIdMap.size();
    }

    public boolean containsNode(long nodeId) {
        return nodeIdMap.containsKey(nodeId);
    }

    public boolean containsNode(Node node) {
        return nodeIdMap.containsKey(node.id);
    }


    public boolean containsEdge(Edge edge) {
        return edgesIdMap.containsKey(edge.id);
    }

    public Set<Edge> getOutputEdges(long nodeId){
        return getHolderOrEmpty(nodeId).outputEdges;
    }

    public Set<Edge> getInputEdges(long nodeId){
        return getHolderOrEmpty(nodeId).inputEdges;
    }

    public List<Node> getSuccessors(long nodeId){
        List<Node> nodes = new ArrayList<>();
        Set<Edge> edges = getHolderOrEmpty(nodeId).outputEdges;

        for (Edge edge : edges) {
            nodes.add(edge.destination);
        }

        return nodes;
    }

    public List<Node> getPredecessors(long nodeId){
        List<Node> nodes = new ArrayList<>();
        Set<Edge> edges = getHolderOrEmpty(nodeId).inputEdges;

        for (Edge edge : edges) {
            nodes.add(edge.source);
        }

        return nodes;
    }

    public void forEachNode(Consumer<Node> action){
        nodeIdMap.values().forEach(action);
    }

    public void forEachEdge(Consumer<Edge> action){
        edgesIdMap.values().forEach(action);
    }

    public void forEachOutputEdge(long nodeId, Consumer<Edge> action){
        getHolderOrEmpty(nodeId).outputEdges.forEach(action);
    }

    public void forEachInputEdge(long nodeId, Consumer<Edge> action){
        getHolderOrEmpty(nodeId).inputEdges.forEach(action);
    }

    public static class EdgeHolder{
        Set<Edge> inputEdges;
        Set<Edge> outputEdges;

        public EdgeHolder() {
            inputEdges = new HashSet<>();
            outputEdges = new HashSet<>();
        }

        public EdgeHolder(Set<Edge> inputEdges, Set<Edge> outputEdges) {
            this.inputEdges = inputEdges;
            this.outputEdges = outputEdges;
        }

        public boolean isEmpty(){
            return inputEdges.isEmpty() && outputEdges.isEmpty();
        }

        public static final EdgeHolder EMPTY_EDGE_HOLDER = new EdgeHolder(Set.of(), Set.of());
    }

    private EdgeHolder getHolderOrEmpty(long nodeId) {
        return nodeEdgeMap.getOrDefault(nodeId, EdgeHolder.EMPTY_EDGE_HOLDER);
    }

    @Override
    public String toString() {
        return "Graph{" +
                "edgesIdMap=" + edgesIdMap +
                ", nodeEdgeMap=" + nodeEdgeMap +
                ", nodeIdMap=" + nodeIdMap +
                '}';
    }
}
