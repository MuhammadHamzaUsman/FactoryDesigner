package org.example.graph.node;

import org.example.factory.Item;

public class SinkNode extends Node {
    double sinkRate;
    Item item;

    public SinkNode(double sinkRate, Item item) {
        super(NodeType.SINK);
        this.sinkRate = sinkRate;
        this.item = item;
    }

    @Override
    public String toString() {
        return "SinkNode{" +
                "sinkRate=" + sinkRate +
                ", item=" + item +
                '}';
    }
}
