package com.example.lsystemgenerator.util;
import java.util.Map;

public record LSystem(String axiom, Map<String,String> rules, int iterations, double angle) {
    public LSystem {
        if (iterations < 0) {throwError("Iterations must be non-negative");}
        if (angle < 0 || angle > 360) {throwError("Angle must be between 0 and 360");}
        if (axiom == null || axiom.isBlank()) {throwError("Axiom cannot be null or blank");}
        if (rules == null || rules.isEmpty()) {throwError("Rules cannot be null or empty");}
        if (rules.keySet().stream().anyMatch(String::isBlank)) {throwError("Rules cannot contain blank keys");}
    }

    private void throwError(String msg){throw new IllegalArgumentException(msg);}

    public LSystem(String axiom, Map<String,String> rules, int iterations) {
        this(axiom, rules, iterations, 30);
    }

    public String generate() {
        System.out.println("Generating LSystem: " + this);

        String currentString = axiom();
        for (int i = 0; i < iterations(); i++) {
            StringBuilder nextString = new StringBuilder();
            for (char c : currentString.toCharArray()) {
                String rule = rules().get(String.valueOf(c));
                if (rule != null) {
                    nextString.append(rule);
                } else {
                    nextString.append(c);
                }
            }
            currentString = nextString.toString();
        }
        return currentString;
    }


    public String toString() {
        return String.format("LSystem(axiom=%s, rules=%s, iterations=%d, angle=%f)", axiom, rules, iterations, angle);
    }
}
