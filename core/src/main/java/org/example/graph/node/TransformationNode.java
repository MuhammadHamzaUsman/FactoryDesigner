package org.example.graph.node;

import org.example.factory.Recipe;

public class TransformationNode extends Node {
    public Recipe recipe;

    public TransformationNode(Recipe recipe) {
        super(NodeType.TRANSFORMATION);
        this.recipe = recipe;
    }

    @Override
    public String[] getName() {
        return new String[]{recipe.machineName, recipe.name};
    }

    @Override
    public String toString() {
        return "TransformationNode{" +
                "recipe=" + recipe +
                ", id='" + id + '\'' +
                '}';
    }
}
