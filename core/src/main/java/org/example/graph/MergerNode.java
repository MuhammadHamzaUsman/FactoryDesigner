package org.example.graph;

import org.example.factory.Item;

public class MergerNode extends Node {
    public final Item item;

    public MergerNode(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "MergerNode{" +
                "item=" + item +
                '}';
    }
}
