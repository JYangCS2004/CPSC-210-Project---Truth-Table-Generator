package model;

import java.util.List;

public class Argument {
    List<LogicExp> premises;
    LogicExp conclusion;

    // REQUIRES: exp must be already in the list of arguments
    // MODIFIES: this
    // EFFECTS: deletes an expression from the argument
    public void deleteExp(LogicExp exp) {
    }

    // REQUIRES: exp is a valid expression
    // MODIFIES: this
    // EFFECTS: adds a new expression to the argument
    public void addExp(LogicExp exp) {

    }

    // EFFECTS: computes each expression for the current truth values,
    //          and returns the values as a list of integers
    public List<Integer> computeEach() {
        return null;
    }


    // REQUIRES: exp is a valid expression
    // MODIFIES: this
    // EFFECTS: sets the expression for the conclusion
    public void changeConclusion(String exp) {

    }

    // EFFECTS: returns the model (set of truth values)
    public AssignModel getModel() {
        return null;
    }

    public List<LogicExp> getExps() {
        return premises;
    }
}
