package org.example.util;

import java.util.Objects;

public class Pair<T, U> {
    public T first;
    public U second;

    public Pair() {
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T component1(){
        return first;
    }

    public U component2(){
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
