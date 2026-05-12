package org.example.graph.node;

import org.example.factory.Item;

public class MergerNode extends Node {
    public final Item item;

    public MergerNode(Item item) {
        super(NodeType.MERGER);
        this.item = item;
    }

    @Override
    public String[] getName() {
        return new String[]{"Merger"};
    }

    @Override
    public String toString() {
        return "MergerNode{" +
                "item=" + item +
                '}';
    }

}
