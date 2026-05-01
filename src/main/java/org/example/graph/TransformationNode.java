package org.example.graph;

import org.example.factory.Recipe;

import java.util.Objects;

public class TransformationNode extends Node {
    Recipe recipe;

    public TransformationNode(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public String toString() {
        return "TransformationNode{" +
                "recipe=" + recipe +
                ", id='" + id + '\'' +
                '}';
    }
}
