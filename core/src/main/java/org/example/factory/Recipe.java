package org.example.factory;

import java.util.LinkedHashMap;

public class Recipe {

    private static long ID_COUNTER = 0;
    public final long id;
    public String name;

    public String machineName;
    public LinkedHashMap<Item, Double> inputMaterials;
    public LinkedHashMap<Item, Double> outputMaterials;

    public Item primaryOutput;

    public Recipe(String name, String machineName, LinkedHashMap<Item, Double> inputMaterials, LinkedHashMap<Item, Double> outputMaterials, Item primaryOutput) {
        this(ID_COUNTER++, name, machineName, inputMaterials, outputMaterials, primaryOutput);
    }

    public Recipe(long id, String name, String machineName, LinkedHashMap<Item, Double> inputMaterials, LinkedHashMap<Item, Double> outputMaterials, Item primaryOutput) {
        if(!outputMaterials.containsKey(primaryOutput)){
            throw new RuntimeException("Primary Output Item not found in output material map.");
        }

        if(name.isBlank()) throw new RuntimeException("Recipe name should not be blank");

        this.id = id;
        this.name = name;
        this.machineName = machineName;
        this.inputMaterials = inputMaterials;
        this.outputMaterials = outputMaterials;
        this.primaryOutput = primaryOutput;
    }

    public static void setCounter(long value){
        ID_COUNTER = value;
    }

    public static long getCounter(){
        return ID_COUNTER;
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
