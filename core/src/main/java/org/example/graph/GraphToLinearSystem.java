package org.example.graph;

import org.example.factory.Recipe;
import org.example.graph.node.MergerNode;
import org.example.graph.node.Node;
import org.example.graph.node.SplitterNode;
import org.example.graph.node.TransformationNode;
import org.example.math.Equation;
import org.example.math.LinearSystem;
import org.example.math.Variable;
import org.example.util.Pair;

import java.util.*;

public class GraphToLinearSystem {
    public static Pair<LinearSystem, Map<Long, Variable>> generateLinearSystem(Graph graph){
        LinearSystem linearSystem = new LinearSystem();

        List<Edge> inputEdges;
        List<Edge> outputEdges;
        List<Equation> equation;

        Map<Long, Variable> variableMapping = initializeVariableMapping(graph.getEdges());

        List<Node> nodes = graph.getNodes().stream().sorted(Comparator.comparing(node -> node.id)).toList();

        for (Node node : nodes) {
            inputEdges = graph.getInputEdges(node.id).stream().toList();
            outputEdges = graph.getOutputEdges(node.id).stream().toList();

            switch (node) {
                case MergerNode mergerNode -> {
                    if (outputEdges.size() > 1) {
                        throw new GraphException("Merger can only hva one output");
                    }

                    equation = buildEquationsFor(mergerNode, inputEdges, outputEdges.getFirst(), variableMapping);

                    for (Equation inputEquations : equation) {
                        linearSystem.insertEquation(inputEquations);
                    }
                }
                case SplitterNode splitterNode -> {
                    if (inputEdges.size() > 1) {
                        throw new GraphException("Merger can only hva one input");
                    }

                    equation = buildEquationsFor(splitterNode, inputEdges.getFirst(), outputEdges, variableMapping);

                    for (Equation inputEquations : equation) {
                        linearSystem.insertEquation(inputEquations);
                    }
                }
                case TransformationNode transformationNode -> {
                    equation = buildEquationsFor(transformationNode, inputEdges, outputEdges, variableMapping);

                    for (Equation inputEquations : equation) {
                        linearSystem.insertEquation(inputEquations);
                    }
                }
                default -> {

                }
            }

        }

        return new Pair<>(linearSystem, variableMapping);
    }

    private static Map<Long, Variable> initializeVariableMapping(Collection<Edge> edges){
        Map<Long, Variable> variableMapping = HashMap.newHashMap(edges.size());

        for (Edge edge : edges) {
            variableMapping.put(edge.id, new Variable(edge.item.name + " - " + edge.id));
        }

        return variableMapping;
    }

    private static List<Equation> buildEquationsFor(SplitterNode node, Edge inputEdge, List<Edge> outputEdges, Map<Long, Variable> variableMap){
        List<Equation> equations = new ArrayList<>();
        Equation equation = new Equation();

        if(!inputEdge.item.equals(node.item)) throw new GraphException("Input Edges should have same item as of splitter");
        equation.insertTerm(variableMap.get(inputEdge.id), 1);

        for (Edge outputEdge : outputEdges) {
            if(!outputEdge.item.equals(node.item)) throw new GraphException("Output Edge should have same item as of splitter");
            equation.insertTerm(variableMap.get(outputEdge.id), -1);
        }

        equations.add(equation);

        Edge referenceEdge = outputEdges.getFirst();
        Variable referenceVariable = variableMap.get(referenceEdge.id);
        double referenceWight = referenceEdge.weight;

        double weightSum = outputEdges.stream().mapToDouble(edge -> edge.weight).sum();
        for (Edge edge : outputEdges.subList(1, outputEdges.size())) {
            equation = new Equation();
            equation.insertTerm(variableMap.get(edge.id), referenceWight / weightSum);
            equation.insertTerm(referenceVariable, -(edge.weight / weightSum));

            equations.add(equation);
        }

        return equations;
    }

    private static List<Equation> buildEquationsFor(MergerNode node, List<Edge> inputEdges, Edge outputEdge, Map<Long, Variable> variableMap){
//        Equation equation = new Equation();
//
//        for (Edge inputEdge : inputEdges) {
//            if(!inputEdge.item.equals(node.item)) throw new GraphException("Input Edges should have same item as of merger");
//
//            equation.insertTerm(variableMap.get(inputEdge.id), 1);
//        }
//
//        if(!outputEdge.item.equals(node.item)) throw new GraphException("Output Edge should have same item as of merger");
//        equation.insertTerm(variableMap.get(outputEdge.id), -1);
//
//        return equation;

        List<Equation> equations = new ArrayList<>();
        Equation equation = new Equation();

        if(!outputEdge.item.equals(node.item)) throw new GraphException("Output Edge should have same item as of merger");
        equation.insertTerm(variableMap.get(outputEdge.id), -1);

        for (Edge inputEdge : inputEdges) {
            if(!inputEdge.item.equals(node.item)) throw new GraphException("Input Edge should have same item as of merger");
            equation.insertTerm(variableMap.get(inputEdge.id), 1);
        }

        equations.add(equation);

        Edge referenceEdge = inputEdges.getFirst();
        Variable referenceVariable = variableMap.get(referenceEdge.id);
        double referenceWight = referenceEdge.weight;

        double weightSum = inputEdges.stream().mapToDouble(edge -> edge.weight).sum();
        for (Edge edge : inputEdges.subList(1, inputEdges.size())) {
            equation = new Equation();
            equation.insertTerm(variableMap.get(edge.id), referenceWight / weightSum);
            equation.insertTerm(referenceVariable, -(edge.weight / weightSum));

            equations.add(equation);
        }

        return equations;
    }

    private static List<Equation> buildEquationsFor(TransformationNode node, List<Edge> inputEdges, List<Edge> outputEdges, Map<Long, Variable> variableMap){
        if (outputEdges.isEmpty()) {
            throw new GraphException("TransformationNode has no outputs");
        }

        List<Equation> equations = new ArrayList<>(); // used to store list of equations for the node
        Recipe recipe = node.recipe; // store recipe of single node

        Edge primaryOutputEdge = outputEdges.stream()
                .filter(edge -> recipe.primaryOutput.equals(edge.item))
                .findFirst()
                .orElseThrow(() -> new GraphException("Primary Output not in output edges"));
        double referenceVariableCoefficient = recipe.outputMaterials.get(primaryOutputEdge.item);
        Variable refreceVariable = variableMap.get(primaryOutputEdge.id);

        for (Edge inputEdge : inputEdges) {
            Equation equation = new Equation();

            if(!recipe.inputMaterials.containsKey(inputEdge.item)){
                throw new GraphException("Input Item " + inputEdge.item + "not found in input edges");
            }

            double itemEdgeCoefficient = recipe.inputMaterials.get(inputEdge.item);
            equation.insertTerm(variableMap.get(inputEdge.id), 1);
            equation.insertTerm(refreceVariable, -(itemEdgeCoefficient / referenceVariableCoefficient));
            equations.add(equation);
        }

        for (Edge outputEdge : outputEdges) {
            if(outputEdge.equals(primaryOutputEdge)) continue;
            Equation equation = new Equation();

            if(!recipe.outputMaterials.containsKey(outputEdge.item)){
                throw new GraphException("Output Item " + outputEdge.item + "not found in output edges");
            }

            double itemEdgeCoefficient = recipe.outputMaterials.get(outputEdge.item);
            equation.insertTerm(variableMap.get(outputEdge.id), 1);
            equation.insertTerm(refreceVariable, -(itemEdgeCoefficient / referenceVariableCoefficient));
            equations.add(equation);
        }
        return equations;
    }
}
