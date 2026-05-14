package org.example.factory;

import java.util.LinkedHashMap;

public class Recipe {

    private static long ID_COUNTER = 0;
    public final long id = ID_COUNTER++;
    public String name;

    public String machineName;
    public LinkedHashMap<Item, Double> inputMaterials;
    public LinkedHashMap<Item, Double> outputMaterials;

    public Item primaryOutput;

    public Recipe(String name, String machineName, LinkedHashMap<Item, Double> inputMaterials, LinkedHashMap<Item, Double> outputMaterials, Item primaryOutput) {
        if(!outputMaterials.containsKey(primaryOutput)){
            throw new RuntimeException("Primary Output Item not found in output material map.");
        }

        if(name.isBlank()) throw new RuntimeException("Recipe name should not be blank");

        this.name = name;
        this.machineName = machineName;
        this.inputMaterials = inputMaterials;
        this.outputMaterials = outputMaterials;
        this.primaryOutput = primaryOutput;
    }

    public static void resetCounter(){
        ID_COUNTER = 0;
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
                ", machineName='" + machineName + '\'' +
                ", inputMaterials=" + inputMaterials +
                ", outputMaterials=" + outputMaterials +
                ", primaryOutput=" + primaryOutput +
                '}';
    }
}
