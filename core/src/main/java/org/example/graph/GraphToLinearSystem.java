package org.example.graph;

import org.example.factory.Recipe;
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

        Map<Long, Variable> variableMapping = initializeVariableMapping(graph.edges);

        graph.sortNodes();

        for (Node node : graph.nodes) {
            inputEdges = graph.collectInputEdgesOfNode(node);
            outputEdges = graph.collectOutputEdgesOfNode(node);

            if(node instanceof MergerNode){
                if(outputEdges.size() > 1){
                    throw new RuntimeException("Merger can only hva one output");
                }

                linearSystem.insertEquation(buildEquationsFor((MergerNode) node, inputEdges, outputEdges.getFirst(), variableMapping));
                continue;
            }

            if(node instanceof SplitterNode){
                if(inputEdges.size() > 1){
                    throw new RuntimeException("Merger can only hva one input");
                }

                equation = buildEquationsFor((SplitterNode) node, inputEdges.getFirst(), outputEdges, variableMapping);

                for (Equation inputEquations : equation) {
                    linearSystem.insertEquation(inputEquations);
                }

                continue;
            }

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

    private static List<Equation> buildEquationsFor(SplitterNode node, Edge inputEdge, List<Edge> outputEdges, Map<Long, Variable> variableMap){
        List<Equation> equations = new ArrayList<>();
        Equation equation = new Equation();

        if(!inputEdge.item.equals(node.item)) throw new RuntimeException("Input Edges should have same item as of splitter");
        equation.insertTerm(variableMap.get(inputEdge.id), 1);

        for (Edge outputEdge : outputEdges) {
            if(!outputEdge.item.equals(node.item)) throw new RuntimeException("Output Edge should have same item as of splitter");
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

    private static Equation buildEquationsFor(MergerNode node, List<Edge> inputEdges, Edge outputEdge, Map<Long, Variable> variableMap){
        Equation equation = new Equation();

        for (Edge inputEdge : inputEdges) {
            if(!inputEdge.item.equals(node.item)) throw new RuntimeException("Input Edges should have same item as of merger");

            equation.insertTerm(variableMap.get(inputEdge.id), 1);
        }

        if(!outputEdge.item.equals(node.item)) throw new RuntimeException("Output Edge should have same item as of merger");
        equation.insertTerm(variableMap.get(outputEdge.id), -1);

        return equation;
    }

    private static List<Equation> buildEquationsFor(TransformationNode node, List<Edge> inputEdges, List<Edge> outputEdges, Map<Long, Variable> variableMap){
        if (outputEdges.isEmpty()) {
            throw new RuntimeException("TransformationNode has no outputs");
        }

        List<Equation> equations = new ArrayList<>(); // used to store list of equations for the node
        Recipe recipe = node.recipe; // store recipe of single node

        Edge primaryOutputEdge = outputEdges.stream()
                .filter(edge -> recipe.primaryOutput.equals(edge.item))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Primary Output not in output edges"));
        double referenceVariableCoefficient = recipe.outputMaterials.get(primaryOutputEdge.item);
        Variable refreceVariable = variableMap.get(primaryOutputEdge.id);

        for (Edge inputEdge : inputEdges) {
            Equation equation = new Equation();

            if(!recipe.inputMaterials.containsKey(inputEdge.item)){
                throw new RuntimeException("Input Item " + inputEdge.item + "not found in input edges");
            }

            double itemEdgeCoefficient = recipe.inputMaterials.get(inputEdge.item);
            equation.insertTerm(variableMap.get(inputEdge.id), 1);
            equation.insertTerm(refreceVariable, -(itemEdgeCoefficient / referenceVariableCoefficient));
            System.out.println(equation);
            System.out.println();
            equations.add(equation);
        }

        for (Edge outputEdge : outputEdges) {
            if(outputEdge.equals(primaryOutputEdge)) continue;
            Equation equation = new Equation();

            if(!recipe.outputMaterials.containsKey(outputEdge.item)){
                throw new RuntimeException("Output Item " + outputEdge.item + "not found in output edges");
            }

            double itemEdgeCoefficient = recipe.outputMaterials.get(outputEdge.item);
            equation.insertTerm(variableMap.get(outputEdge.id), 1);
            equation.insertTerm(refreceVariable, -(itemEdgeCoefficient / referenceVariableCoefficient));
            System.out.println(equation);
            System.out.println();
            equations.add(equation);
        }
        return equations;
    }
}
