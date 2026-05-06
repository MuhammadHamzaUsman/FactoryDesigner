package org.example.factory;

import java.util.*;

public class Machine {
    private static long COUNTER_ID = 0;
    public long id = COUNTER_ID++;

    public final Set<Recipe> recipes;
    public final Map<Item, List<Recipe>> inputItemRecipeMap;
    public final Map<Item, List<Recipe>> outputItemRecipeMap;

    public Machine(LinkedHashSet<Recipe> recipes) {
        this.recipes = recipes;
        this.inputItemRecipeMap = new LinkedHashMap<>();
        this.outputItemRecipeMap = new LinkedHashMap<>();

        for (Recipe recipe : recipes) {
            for (Item item : recipe.inputMaterials.keySet()) {
                inputItemRecipeMap.putIfAbsent(item, new ArrayList<>());
                inputItemRecipeMap.get(item).add(recipe);
            }

            for (Item item : recipe.outputMaterials.keySet()) {
                inputItemRecipeMap.putIfAbsent(item, new ArrayList<>());
                inputItemRecipeMap.get(item).add(recipe);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return id == machine.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Machine{" +
                "id=" + id +
                ", recipes=" + recipes +
                '}';
    }
}
