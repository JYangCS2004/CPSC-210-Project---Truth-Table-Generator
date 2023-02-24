package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Argument {
    private List<LogicExp> premises;
    private LogicExp conclusion;
    private AssignModel model;

    public Argument() {
        premises = new ArrayList<>();
        model = new AssignModel();
    }

    // REQUIRES: exp must be already in the list of arguments
    // MODIFIES: this
    // EFFECTS: deletes an expression from the argument, re-adjusts
    //          the counter
    public void deleteExp(int num) {
        LogicExp exp = premises.get(num - 1);
        premises.remove(num - 1);
        for (String s : exp.getSymbolsUsed()) {
            if (isSymbolNotInOtherExps(s)) {
                model.delete(s);
            }
        }
    }

    // REQUIRES: exp is a valid expression
    // MODIFIES: this
    // EFFECTS: adds a new expression to the argument, re-adjusts
    //          the counter
    public void addExp(LogicExp exp) {
        premises.add(exp);

        for (String symbol : exp.getSymbolsUsed()) {
            if (!model.isInSet(symbol)) {
                model.add(symbol);
            }
        }
    }

    // EFFECTS: computes each expression for the current truth values,
    //          and returns the values as a list of integers
    public List<Integer> computeEach() {
        List<Integer> results = new ArrayList<>();
        for (LogicExp e : premises) {
            results.add(e.evaluate(model));
        }

        results.add(conclusion.evaluate(model));
        return results;
    }


    // REQUIRES: exp is a valid expression
    // MODIFIES: this
    // EFFECTS: sets the expression for the conclusion
    public void setConclusion(LogicExp exp) {
        this.conclusion = exp;
    }

    public LogicExp getConclusion() {
        return conclusion;
    }


    // EFFECTS: returns the model (set of truth values)
    public AssignModel getModel() {
        return model;
    }

    public List<LogicExp> getExps() {
        return premises;
    }

    // EFFECTS: returns true if symbol is not in the other expressions
    private boolean isSymbolNotInOtherExps(String symbol) {
        boolean result = true;
        for (LogicExp e : premises) {
            if (e.getSymbolsUsed().contains(symbol)) {
                result = false;
                break;
            }
        }

        if (result) {
            result = !conclusion.getSymbolsUsed().contains(symbol);
        }

        return result;
    }

}
