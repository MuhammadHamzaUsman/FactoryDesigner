package org.example.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Node{
    private static long ID_COUNTER = 0;
    public final long id = ID_COUNTER++;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
