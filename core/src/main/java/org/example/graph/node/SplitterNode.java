package org.example.graph.node;

import org.example.factory.Item;

public class SplitterNode extends Node {
    public final Item item;

    public SplitterNode(Item item) {
        super(NodeType.SPLITTER);
        this.item = item;
    }

    @Override
    public String toString() {
        return "SplitterNode{" +
                "item=" + item +
                '}';
    }
}
