package org.example.math;

import java.util.*;

public class LinearSystem {
    // list of all equations in a linear system ie
    // 2a + 3b + c = 0
    // 3b + c + d + 2 = 0
    public final List<Equation> equations = new ArrayList<>();

    // set of all variable in system ie
    // a, b, c, d
    public final Set<Variable> variables = new LinkedHashSet<>();

    public void insertEquation(Equation equation){
        equations.add(equation);

        variables.addAll(equation.terms.keySet());
    }

    public void injectVariablesValue(Variable variable, double coefficient){
        Equation equation = new Equation();
        equation.insertTerm(variable, 1);
        equation.constant = coefficient;
        insertEquation(equation);
    }

    @Override
    public String toString() {
        return "LinearSystem{" +
                "equations=" + equations +
                "\nvariables=" + variables +
                '}';
    }
}
