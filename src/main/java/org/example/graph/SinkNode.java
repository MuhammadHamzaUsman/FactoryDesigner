package org.example.graph;

import org.example.factory.Item;

import java.util.Objects;

public class SinkNode extends Node {
    double sinkRate;
    Item item;

    public SinkNode(double sinkRate, Item item) {
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
