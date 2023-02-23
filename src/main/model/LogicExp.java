package model;

import java.util.*;

public class LogicExp {

    private String expression;
    private List<String> symbols;

    // EFFECTS: constructs a new logical expression based on the given string
    public LogicExp(String exp) {
        this.expression = removeSpaces(exp);
    }

    // REQUIRES: isValid() returns true
    // EFFECTS: evaluates this expression based on given truth values
    public int evaluate(AssignModel values) {
        Stack<Character> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();

        int i = 0;
        while (i < expression.length()) {
            char curr = expression.charAt(i);
            if (Character.isLetter(curr)) {
                operands.push(values.getValForSymbol(Character.toString(curr)));
            } else if (curr == ')') {
                popUntilOpenBracket(operators, operands);
            } else {
                if (curr != '(') {
                    popAllHigher(operators, operands, curr);
                } else {
                    operators.push(curr);
                }
            }
            i++;
        }
        return operands.pop();
    }


    // MODIFIES: operators, operands
    // EFFECTS: computes the operators until a closing bracket is found
    private void popUntilOpenBracket(Stack<Character> operators, Stack<Integer> operands) {
        while (!operators.empty()) {
            char curr = operators.pop();
            if (curr == '~') {
                Operator operatorNeg = new Operator('~');
                int result = operatorNeg.evaluateNegator(operands.pop());
                operands.push(result);
            } else if (curr == '(') {
                break;
            } else {
                Operator operator = new Operator(curr);
                int op2 = operands.pop();
                int op1 = operands.pop();
                operands.push(operator.evaluate(op1, op2));
            }
        }
    }

    private void popAllHigher(Stack<Character> operators, Stack<Integer> operands, char curr) {
        while (!operators.empty()) {
            Operator prev = new Operator(operators.peek());

            if (prev.isHigherRank(curr)) {
                operators.pop();
                int result = prev.evaluateNegator(operands.pop());
                operands.push(result);
            } else {
                break;
            }
        }

        operators.push(curr);
    }


    // EFFECTS: returns the symbols that the expression requires for it to be evaluated
    public List<String> getSymbolsUsed() {
        return null;
    }

    // EFFECTS: returns true is expression is valid (can be evaluated)
    public boolean isValid() {
        return false;
    }

    // EFFECTS: returns the expression as a string
    public String getExpString() {
        return expression;
    }

    // MODIFIES: this
    // EFFECTS: removes extra spaces within in the input string, and returns the new string
    private String removeSpaces(String input) {
        String temp = "";

        int i = 0;
        while (i < input.length()) {
            if (input.charAt(i) != ' ') {
                temp += Character.toString(input.charAt(i));
            }

            i++;
        }

        return temp;
    }


}
