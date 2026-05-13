package org.example.graph.node;

import org.example.factory.Item;

public class SinkNode extends Node {
    public final Item item;

    public SinkNode(Item item) {
        super(NodeType.SINK);
        this.item = item;
    }

    @Override
    public String[] getName() {
        return new String[] {"Splitter"};
    }

    @Override
    public String toString() {
        return "SinkNode{" +
                ", item=" + item +
                '}';
    }
}
