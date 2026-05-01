package org.example.math;

import org.apache.commons.math3.linear.*;

import java.util.HashMap;
import java.util.Map;

// used Gaussian elimination with partial pivoting methods to solve linear system
public class LinearSystemSolver {
    public static Map<Variable, Double> solveSystem(LinearSystem linearSystem){

        // we make a matrix of columns x row
        // there are n equation so n rows
        // there are m variable so m columns
        int rows = linearSystem.equations.size();
        int columns = linearSystem.variables.size();
        Map<Variable, Integer> variableMap = variableMapping(linearSystem);

        RealMatrix matrix = MatrixUtils.createRealMatrix(initializeMatrix(linearSystem, columns, rows, variableMap));
        RealVector vector = new ArrayRealVector(initializeVector(linearSystem, rows));

        QRDecomposition decomposition = new QRDecomposition(matrix);
        DecompositionSolver solver = decomposition.getSolver();

        if (!solver.isNonSingular()) {
            throw new RuntimeException("System is not solvable");
        }

        RealVector solution = solver.solve(vector);

        Map<Variable, Double> variableAnswers = new HashMap<>();

        variableMap.forEach( (variable, integer) ->
                variableAnswers.put(variable, solution.getEntry(integer))
        );

        return variableAnswers;
    }

    private static Map<Variable, Integer> variableMapping(LinearSystem linearSystem){
        HashMap<Variable, Integer> variableMap = new HashMap<>();

        int columnsIndex = 0;
        for(Variable variable: linearSystem.variables){
            variableMap.put(variable, columnsIndex++);
        }

        return variableMap;
    }

    private static double[][] initializeMatrix(
            LinearSystem linearSystem,
            int columns,
            int rows,
            Map<Variable, Integer> variableMap
    ) {
        double[][] matrix = new double[rows][columns];

        int equationRow = 0;
        for (Equation equation: linearSystem.equations) {
            for (Map.Entry<Variable, Double> entry: equation.terms.entrySet()){
                matrix[equationRow][variableMap.get(entry.getKey())] = entry.getValue();
            }

            equationRow++;
        }

        return matrix;
    }

    private static double[] initializeVector(LinearSystem linearSystem, int rows){
        double[] vector = new double[rows];

        int equationRow = 0;
        for (Equation equation: linearSystem.equations) {
            vector[equationRow++]= equation.constant;
        }

        return vector;
    }
}
