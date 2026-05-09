package org.example.graph.node;

import org.example.factory.Item;

public class SourceNode extends Node{
    double productionRate;
    public final Item item;

    public SourceNode(double productionRate, Item item) {
        super(NodeType.SOURCE);
        this.productionRate = productionRate;
        this.item = item;
    }

    @Override
    public String toString() {
        return "SourceNode{" +
                "productionRate=" + productionRate +
                ", item=" + item +
                ", id='" + id + '\'' +
                '}';
    }
}