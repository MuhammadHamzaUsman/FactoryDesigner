package org.example.math;

import org.example.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Equation {
    // terms store variable and their coefficient
    // 2a - 3b = 0 would become
    // key(a) to value(2) and key(b) to value(-3)
    // 0 constant
    public final Map<Variable, Double> terms = new HashMap<>();
    public double constant = 0.0;

    public void insertTerm(Variable variable, double coefficient){
        terms.merge(variable, coefficient, Double::sum);
    }

    public Double getCoefficient(Variable variable){
        return terms.get(variable);
    }

    public Set<Variable> getVariables() {
        return terms.keySet();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        terms.forEach(((variable, coeff) -> sb.append(coeff).append(variable).append(" ") ));
        sb.append("= ").append(constant);

        return sb.toString();
    }
}
