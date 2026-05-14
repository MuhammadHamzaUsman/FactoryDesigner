package org.example.factory;

import kotlinx.serialization.Serializable;

import java.util.Objects;

@Serializable
public class Item {

    private static long ID_COUNTER = 0;
    public transient final long id = ID_COUNTER++;

    public final String name;

    public Item(String name) {
        if(name.isBlank()) throw new RuntimeException("Item name should not be blank");

        this.name = name;
    }

    public static void resetCounter(){
        ID_COUNTER = 0;
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
