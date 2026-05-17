package org.example.graph.node;

public abstract class Node{
    private static long ID_COUNTER = 0;
    public final long id;
    public final NodeType type;

    public Node(NodeType type) {
        this(ID_COUNTER++, type);
    }

    public Node(long id, NodeType type){
        this.id = id;
        this.type = type;
    }

    public static void setCounter(long value){
        ID_COUNTER = value;
    }

    public static long getCounter(){
        return ID_COUNTER;
    }

    abstract public String[] getName();

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
