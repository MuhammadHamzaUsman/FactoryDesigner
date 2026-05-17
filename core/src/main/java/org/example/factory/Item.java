package org.example.factory;

import kotlinx.serialization.Serializable;

import java.util.Objects;

@Serializable
public class Item {

    private static long ID_COUNTER = 0;
    public final long id;

    public final String name;

    public Item(String name) {
        this(ID_COUNTER++, name);
    }

    public Item(long id, String name){
        if(name.isBlank()) throw new RuntimeException("Item name should not be blank");

        this.name = name;
        this.id = id;
    }
    public static void setCounter(long value){
        ID_COUNTER = value;
    }

    public static long getCounter() {
        return ID_COUNTER;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
