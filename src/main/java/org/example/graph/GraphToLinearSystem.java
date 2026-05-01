package org.example.graph;

import org.example.factory.Item;
import org.example.factory.Recipe;
import org.example.math.Equation;
import org.example.math.LinearSystem;
import org.example.math.Variable;
import org.example.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class GraphToLinearSystem {
    public static Pair<LinearSystem, Map<Long, Variable>> generateLinearSystem(Graph graph){
        LinearSystem linearSystem = new LinearSystem();

        List<Edge> inputEdges;
        List<Edge> outputEdges;
        List<Equation> equation;

        Map<Long, Variable> variableMapping = initializeVariableMapping(graph.edges);

        graph.sortNodes();

        for (Node node : graph.nodes) {
            inputEdges = graph.collectInputEdgesOfNode(node);
            outputEdges = graph.collectOutputEdgesOfNode(node);

            if(node instanceof TransformationNode){

                equation = buildEquationsFor((TransformationNode) node, inputEdges, outputEdges, variableMapping);

                for (Equation inputEquations : equation) {
                    linearSystem.insertEquation(inputEquations);
                }
            }
        }

        return new Pair<>(linearSystem, variableMapping);
    }

    private static Map<Long, Variable> initializeVariableMapping(List<Edge> edges){
        Map<Long, Variable> variableMapping = HashMap.newHashMap(edges.size());

        for (Edge edge : edges) {
            variableMapping.put(edge.id, new Variable(edge.item.name + " - " + edge.id));
        }

        return variableMapping;
    }

    private static List<Equation> buildEquationsFor(TransformationNode node, List<Edge> inputEdges, List<Edge> outputEdges, Map<Long, Variable> variableMap){
        if (outputEdges.isEmpty()) {
            throw new RuntimeException("TransformationNode has no outputs");
        }

        List<Equation> equations = new ArrayList<>(); // used to store list of equations for the node
        Recipe recipe = node.recipe; // store recipe of single node

        // Select the all edges with primary Output as item from list of outputEdges
        double referenceVariableCoefficient = recipe.outputMaterials.get(recipe.primaryOutput);
        List<Edge> referenceItemEdges = outputEdges.stream()
                .filter(edge -> edge.item.equals(recipe.primaryOutput))
                .toList();

        double edgeItemCoefficient;

        // group item by edges this will be used to then used to make a single equation for all edges with same item
        Map<Item, List<Edge>> itemGroups = groupEdgesByItem(inputEdges);
        for (Item inputItem: itemGroups.keySet()) {
            if (!recipe.inputMaterials.containsKey(inputItem)) {
                throw new RuntimeException("Input item not found");
            }

            edgeItemCoefficient = recipe.inputMaterials.get(inputItem);
            addEquationsForItemEdges(itemGroups.get(inputItem), edgeItemCoefficient, referenceItemEdges, referenceVariableCoefficient, variableMap, equations);
        }

        itemGroups = groupEdgesByItem(outputEdges);
        for (Item outputItem: itemGroups.keySet()) {
            if(outputItem.equals(recipe.primaryOutput)) continue;

            if (!recipe.outputMaterials.containsKey(outputItem)) {
                throw new RuntimeException("Output item not found");
            }

            edgeItemCoefficient = recipe.outputMaterials.get(outputItem);
            addEquationsForItemEdges(itemGroups.get(outputItem), edgeItemCoefficient, referenceItemEdges, referenceVariableCoefficient, variableMap, equations);
        }

        // same thing done for reference Item Edge as for other edges
        if(referenceItemEdges.size() > 1){
            addEquationsForProportionalInputFlow(referenceItemEdges, variableMap, equations);
        }

        return equations;
    }

    private static void addEquationsForItemEdges(
            List<Edge> itemEdges,
            double edgeItemCoefficient,
            List<Edge> referenceItemEdges,
            double referenceItemCoefficient,
            Map<Long, Variable> variableMap,
            List<Equation> equations
    ) {
        Equation equation = new Equation();

        // add each edge of item in equation for splitting purpose
        for (Edge itemEdge : itemEdges) {
            equation.insertTerm(variableMap.get(itemEdge.id), 1);
        }

        // add each edge of referenceItem into this equation for splitting primary Item
        for (Edge referenceItemEdge: referenceItemEdges) {
            equation.insertTerm(variableMap.get(referenceItemEdge.id), -(edgeItemCoefficient / referenceItemCoefficient));
        }

        equations.add(equation);

        // Applies proportional split between multiple edges of same item
        // i take first edge as reference variable choice could be any edge
        // input can be proportional only if there are more than 1 inputs
        // else first input is 100 percent
        if(itemEdges.size() > 1){
            addEquationsForProportionalInputFlow(itemEdges, variableMap, equations);
        }

    }

    private static void addEquationsForProportionalInputFlow(List<Edge> edges, Map<Long, Variable> variableMap, List<Equation> equations) {
        // assume first edge is referenceEdge
        Edge referenceEdge = edges.getFirst();
        Variable referenceVariable = variableMap.get(referenceEdge.id);
        double referenceWight = referenceEdge.weight;
        Equation equation;

        double weightSum = edges.stream().mapToDouble(edge -> edge.weight).sum();
        for (Edge edge : edges.subList(1, edges.size())) {
            equation = new Equation();
            equation.insertTerm(variableMap.get(edge.id), referenceWight / weightSum);
            equation.insertTerm(referenceVariable, -(edge.weight / weightSum));

            equations.add(equation);
        }
    }

    private static Map<Item, List<Edge>> groupEdgesByItem(List<Edge> edges){
        return edges.stream().collect(
            Collectors.groupingBy(edge -> edge.item)
        );
    }
}
