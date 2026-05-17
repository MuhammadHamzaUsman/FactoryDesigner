package org.example.graph.node;

import org.example.factory.Item;

public class SplitterNode extends Node {
    public final Item item;

    public SplitterNode(Item item) {
        super(NodeType.SPLITTER);
        this.item = item;
    }

    public SplitterNode(long id, Item item) {
        super(id, NodeType.SPLITTER);
        this.item = item;
    }

    @Override
    public String[] getName() {
        return new String[]{"Sink"};
    }

    @Override
    public String toString() {
        return "SplitterNode{" +
                "item=" + item +
                '}';
    }
}
