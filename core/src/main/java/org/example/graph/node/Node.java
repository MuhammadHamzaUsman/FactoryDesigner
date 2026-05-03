package org.example.graph.node;

import java.util.Objects;

public abstract class Node{
    private static long ID_COUNTER = 0;
    public final long id = ID_COUNTER++;
    public final NodeType type;

    public Node(NodeType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return id == node.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
