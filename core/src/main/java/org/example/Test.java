package org.example;

import org.example.factory.Item;
import org.example.factory.Recipe;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.GraphToLinearSystem;
import org.example.graph.node.Node;
import org.example.graph.node.SinkNode;
import org.example.graph.node.SourceNode;
import org.example.graph.node.TransformationNode;
import org.example.math.Equation;
import org.example.math.LinearSystem;
import org.example.math.LinearSystemSolver;
import org.example.math.Variable;
import org.example.util.Pair;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Test {
    static Map<String, Item> items = new HashMap<>();
    static Map<String, Recipe> recipes = new HashMap<>();

    public static void hello() {
        // creating Items
        items.put("Iron Ore", new Item("Iron Ore"));
        items.put("Iron Ingot", new Item("Iron Ingot"));
        items.put("Iron Plate", new Item("Iron Plate"));
        items.put("Iron Screw", new Item("Iron Screw"));
        items.put("Frame", new Item("Frame"));
        items.put("Scrap", new Item("Scrap"));

        // creating Recipe
        recipes.put("Iron Recycling", new Recipe(
                "Iron Recycling",
                "Cons 1",
                LinkedHashMapOf(items.get("Scrap"), 2.0),
                LinkedHashMapOf(items.get("Iron Ore"), 1.0),
                items.get("Iron Ore")
        ));

        recipes.put("Iron Smelting", new Recipe(
                "Iron Smelting",
                "Cons 1",
                LinkedHashMapOf(items.get("Iron Ore"), 2.0),
                LinkedHashMapOf(items.get("Iron Ingot"), 1.0),
                items.get("Iron Ingot")
        ));

        recipes.put("Iron Plates", new Recipe(
                "Iron Plates",
                "Cons 1",
                LinkedHashMapOf(items.get("Iron Ingot"), 4.0),
                LinkedHashMapOf(items.get("Iron Plate"), 3.0),
                items.get("Iron Plate")
        ));

        recipes.put("Iron Light Frames", new Recipe(
                "Iron Light Frames",
                "Cons 1",
                LinkedHashMapOf(items.get("Iron Plate"), 6.0, items.get("Iron Screw"), 16.0),
                LinkedHashMapOf(items.get("Frame"), 2.0, items.get("Scrap"), 4.0),
                items.get("Frame")
        ));

//         creating node
        Node recycler = new TransformationNode(recipes.get("Iron Recycling"));
        Node source1 = new SourceNode(0, items.get("Iron Ore"));
        Node source2 = new SourceNode(0, items.get("Iron Ore"));
        Node smelter = new TransformationNode(recipes.get("Iron Smelting"));
        Node constructor1 = new TransformationNode(recipes.get("Iron Plates"));
        Node constructor2 = new TransformationNode(recipes.get("Iron Light Frames"));
        Node frameSink = new SinkNode(0.0, items.get("Frame"));
        List<Node> nodes = List.of(source1, smelter, constructor1, constructor2, frameSink, recycler, source2);

        // connecting them
        Edge src1Sem = new Edge(source1, items.get("Iron Ore"), smelter);
        Edge src2Sem = new Edge(source2, items.get("Iron Ore"), smelter);
        Edge smeCons1 = new Edge(smelter, items.get("Iron Ingot"), constructor1);
        Edge cons1Cons2 = new Edge(constructor1, items.get("Iron Plate"), constructor2);
        Edge screwCons2 = new Edge(null, items.get("Iron Screw"), constructor2);
        Edge cons2snk1Frame = new Edge(constructor2, items.get("Frame"), frameSink);
        Edge cons2snk2Frame = new Edge(constructor2, items.get("Frame"), frameSink);
        Edge cons2RecycleScrap = new Edge(constructor2, items.get("Scrap"), recycler);
        Edge recycleSme = new Edge(recycler, items.get("Iron Ore"), smelter);
        List<Edge> edges = List.of(src1Sem, src2Sem, cons2snk2Frame, cons2RecycleScrap, recycleSme, smeCons1, cons1Cons2, cons2snk1Frame, screwCons2);

        cons2snk1Frame.weight = 1;
        cons2snk2Frame.weight = 1;

        Graph graph = new Graph(edges);

        Pair<LinearSystem, Map<Long, Variable>> pair = GraphToLinearSystem.generateLinearSystem(graph);
        LinearSystem linearSystem = pair.first;
        Map<Long, Variable> edgeVariableMap = pair.second;

        linearSystem.injectVariablesValue(edgeVariableMap.get(smeCons1.id), 10);

        for (Edge edge : edges) {
            System.out.println("Item: " + edge.item + ", Variable Id: " + edgeVariableMap.get(edge.id).id);
        }

        for (Equation equation : linearSystem.equations) {
            System.out.println(equation);
        }

        Map<Variable, Double> variableAnswerMap = LinearSystemSolver.solveSystem(linearSystem);

        System.out.println(variableAnswerMap);
    }


    public static void testSimpleLoop() {
        Item item = new Item("Iron");

        recipes.put("Iron Recycling", new Recipe(
                "Iron Recycling",
                "Cons 1",
                LinkedHashMapOf(item, 2.0),
                LinkedHashMapOf(item, 1.0),
                item
        ));

        recipes.put("Iron Waster", new Recipe(
                "Iron Waster",
                "Cons 2",
                LinkedHashMapOf(item, 1.0),
                LinkedHashMapOf(item, 2.0),
                item
        ));


        Node A_node = new SourceNode(0, item);
        Node B_node = new TransformationNode(recipes.get("Iron Waster")); // dummy passthrough
        Node C_node = new TransformationNode(recipes.get("Iron Recycling"));

        // cycle edges
        Edge A_to_B = new Edge(A_node, item, B_node);
        Edge B_to_C = new Edge(B_node, item, C_node);
        Edge C_to_A = new Edge(C_node, item, A_node);

        A_to_B.weight = 1;
        B_to_C.weight = 1;
        C_to_A.weight = 1;

        Graph graph = new Graph(List.of(A_to_B, B_to_C, C_to_A));

        Pair<LinearSystem, Map<Long, Variable>> result =
                GraphToLinearSystem.generateLinearSystem(graph);

        LinearSystem system = result.first;
        Map<Long, Variable> vars = result.second;

        // fix scale
        system.injectVariablesValue(vars.get(A_to_B.id), 10.0);

        Map<Variable, Double> solution =
                LinearSystemSolver.solveSystem(system);

        double a = solution.get(vars.get(A_to_B.id));
        double b = solution.get(vars.get(B_to_C.id));
        double c = solution.get(vars.get(C_to_A.id));

        System.out.println(a + " " + b + " " + c);

        System.out.println(10.0 + " " + a + " " + 1e-6);
        System.out.println(20.0 + " " + b + " " + 1e-6);
        System.out.println(10.0 + " " + c + " " + 1e-6);
    }
    private static LinkedHashMap<Item, Double> LinkedHashMapOf(Item item, double amount){
        LinkedHashMap<Item, Double> map = new LinkedHashMap<>();
        map.put(item, amount);

        return map;
    }



    private static LinkedHashMap<Item, Double> LinkedHashMapOf(Item k1, double v1, Item k2, double v2) {
        LinkedHashMap<Item, Double> map = new LinkedHashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);

        return map;
    }
}

