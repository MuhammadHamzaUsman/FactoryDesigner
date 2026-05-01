package org.example.factory;

import java.util.Map;
import java.util.Objects;

public class Recipe {

    private static long ID_COUNTER = 0;
    public final long id = ID_COUNTER++;
    public String name;
    public Map<Item, Double> inputMaterials;
    public Map<Item, Double> outputMaterials;

    public Item primaryOutput;

    public Recipe(String name, Map<Item, Double> inputMaterials, Map<Item, Double> outputMaterials, Item primaryOutput) {
        if(!outputMaterials.containsKey(primaryOutput)){
            throw new RuntimeException("Primary Output Item not found in output material map.");
        }

        this.name = name;
        this.inputMaterials = inputMaterials;
        this.outputMaterials = outputMaterials;
        this.primaryOutput = primaryOutput;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return id == recipe.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", inputMaterials=" + inputMaterials +
                ", outputMaterials=" + outputMaterials +
                ", primaryOutput=" + primaryOutput +
                '}';
    }
}
