package org.example.math;

import java.util.Objects;

public class Variable {

    private static long ID_COUNTER = 0;

    // name of variable
    public final long id = ID_COUNTER++;
    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(id, variable.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
