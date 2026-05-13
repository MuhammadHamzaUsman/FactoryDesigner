package org.example.graph.node;

import org.example.factory.Item;

public class SourceNode extends Node{
    public final Item item;

    public SourceNode(Item item) {
        super(NodeType.SOURCE);
        this.item = item;
    }

    @Override
    public String[] getName() {
        return new String[]{"Source"};
    }

    @Override
    public String toString() {
        return "SourceNode{" +
                ", item=" + item +
                ", id='" + id + '\'' +
                '}';
    }
}