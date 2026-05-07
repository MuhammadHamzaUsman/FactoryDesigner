package org.example.data;

import org.example.factory.Item;
import org.example.factory.Recipe;

import java.util.LinkedHashMap;

public class ItemAndRecipeState {
    public LinkedHashMap<Long, Item> items;
    public LinkedHashMap<Long, Recipe> recipes;

    public ItemAndRecipeState(LinkedHashMap<Long, Item> items, LinkedHashMap<Long, Recipe> recipes) {
        this.items = items;
        this.recipes = recipes;
    }
}
