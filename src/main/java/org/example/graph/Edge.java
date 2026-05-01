package org.example.graph;

import org.example.factory.Item;

import java.util.Objects;

public class Edge {

    private static long ID_COUNTER = 0;
    public final long id = ID_COUNTER++;
    public final Node source;

    public final Item item;

    public final Node destination;

    // weight is used when a node have multiple input edges of same item
    // then weight of all edges are added up and the assigned to each edge as
    // weight / sum
    // it must be >= 1
    public float weight = 1;

    public Edge(Node source, Item item, Node destination) {
        if(source == null && destination == null){
            throw new RuntimeException("both source and destination can not be null");
        }

        this.source = source;
        this.item = item;
        this.destination = destination;
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
                "source=" + source +
                ", item=" + item +
                ", destination=" + destination +
                '}';
    }
}
