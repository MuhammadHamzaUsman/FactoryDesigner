package org.example.graph;

import org.example.factory.Item;

public class SourceNode extends Node{
    double productionRate;
    Item item;

    public SourceNode(double productionRate, Item item) {
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