package model;

import java.util.*;

public class LogicExp {

    private String expression;
    private List<String> symbols;

    // EFFECTS: constructs a new logical expression based on the given string
    public LogicExp(String exp) {
        symbols = new ArrayList<>();
        this.expression = removeSpaces(exp);
    }

    // REQUIRES: isValid() returns true
    // EFFECTS: evaluates this expression based on given truth values
    public int evaluate(AssignModel values) {
        Stack<Character> operators = new Stack<>();
        Stack<Integer> operands = new Stack<>();
        String temp = "(" + expression + ")";

        int i = 0;
        while (i < temp.length()) {
            char curr = temp.charAt(i);
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
        return symbols;
    }

    // TODO: finish implementing isValid()
    // EFFECTS: returns true is expression is valid (can be evaluated)
    public boolean isValid() {
        if (hasNoIllegalCharacters()) {
            if (checkPosOfOperands()) {
                return testCorrectOperatorsWithinBrackets();
            }
            return false;
        }

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
            char currChar = input.charAt(i);
            if (currChar != ' ') {
                temp += currChar;

                if (Character.isLetter(input.charAt(i)) && !symbols.contains(Character.toString(input.charAt(i)))) {
                    symbols.add(Character.toString(currChar));
                }
            }

            i++;
        }

        return temp;
    }

    public boolean checkPosOfOperands() {
        String temp = "(" + expression.replaceAll("~", "Q~") + ")";

        int rank = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isLetter(temp.charAt(i))) {
                rank++;
            } else if (temp.charAt(i) == '(' || temp.charAt(i) == ')') {
                continue;
            } else {
                rank--;
            }

            if (rank != 0 && rank != 1) {
                return false;
            }
        }

        if (rank != 1) {
            return false;
        }

        return true;
    }


    public boolean testCorrectOperatorsWithinBrackets() {
        Stack<Character> operators = new Stack<>();
        String temp = "(" + expression + ")";
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == ')') {
                if (!removeUntilBracket(operators, i, temp)) {
                    return false;
                }
            } else if (!Character.isLetter(temp.charAt(i))) {
                operators.push(temp.charAt(i));
            }
        }

        return operators.empty();
    }


    private boolean removeUntilBracket(Stack<Character> ops, int i, String exp) {
        List<Character> opList = new ArrayList<>();
        boolean isFound = false;
        boolean isAllEqual = true;
        while (!ops.empty()) {
            char curr = ops.pop();
            if (curr == '(') {
                isFound = true;
                if (!checkAllEqual(opList, ops)) {
                    isAllEqual = false;
                }

                break;
            } else if (exp.charAt(i) != '~') {
                opList.add(curr);
            }
        }

        return isAllEqual && isFound;
    }

    private boolean checkAllEqual(List<Character> op, Stack<Character> o) {
        if (op.isEmpty()) {
            return true;
        }

        char first = op.get(0);
        boolean allSame = true;
        for (char c : op) {
            if (c != first) {
                allSame = false;
                break;
            }
        }

        if (allSame) {
            return first == '&' || first == '|';
        }

        return false;
    }


    public boolean hasNoIllegalCharacters() {
        // TODO: fix this: didn't implement XOR yet
        String allowChars = "~|&=>()";
        for (char c : expression.toCharArray()) {
            if (!Character.isLetter(c) && !allowChars.contains(Character.toString(c))) {
                return false;
            }
        }
        return true;
    }
}
