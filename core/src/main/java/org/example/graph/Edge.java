package org.example.graph;

import org.example.factory.Item;
import org.example.graph.node.Node;

public class Edge {

    private static long ID_COUNTER = 0;
    public final long id;
    public Node source;

    public final Item item;

    public Node destination;

    // weight is used when a node have multiple input edges of same item
    // then weight of all edges are added up and the assigned to each edge as
    // weight / sum
    // it must be >= 1
    public float weight = 1;

    public Edge(Node source, Item item, Node destination) {
        this(ID_COUNTER++, source, item, destination);
    }

    public Edge(long id, Node source, Item item, Node destination) {
        if(source == null && destination == null){
            throw new RuntimeException("both source and destination can not be null");
        }

        this.id = id;
        this.source = source;
        this.item = item;
        this.destination = destination;
    }

    public static void setCounter(long value){
        ID_COUNTER = value;
    }

    public static long getCounter(){
        return ID_COUNTER;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return id == edge.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id" + id +
                ", source=" + source +
                ", item=" + item +
                ", destination=" + destination +
                '}';
    }
}
