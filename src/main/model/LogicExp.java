package model;

import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// abstract representation of a logical expression
public class LogicExp {
    private String expression;
    private List<String> symbols;
    private static final Pattern ILLEGAL_PATTERN = Pattern.compile("[(][|&=>]|[|&=>][)]");
    private static final String ALLOWED_OPS = "~|&=>()*";


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


    // MODIFIES: operators, operands
    // EFFECTS: pops all operators from the stack of greater rank than the given operator,
    //          and pushes the results to the operand stack
    private void popAllHigher(Stack<Character> operators, Stack<Integer> operands, char curr) {
        while (true) {
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


    // EFFECTS: returns true is expression is valid (can be evaluated)
    public boolean isValid() {
        if (hasNoIllegalChars()) {
            Matcher testExp = ILLEGAL_PATTERN.matcher(expression);

            if (testExp.find()) {
                return false;
            }

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
        StringBuilder temp = new StringBuilder();

        int i = 0;
        while (i < input.length()) {
            char currChar = input.charAt(i);
            if (currChar != ' ') {
                temp.append(currChar);

                if (Character.isLetter(input.charAt(i)) && !symbols.contains(Character.toString(input.charAt(i)))) {
                    symbols.add(Character.toString(currChar));
                }
            }

            i++;
        }

        return temp.toString();
    }


    // EFFECTS: returns true if the operands and operators are in the right positions
    //          with respect to each other
    public boolean checkPosOfOperands() {
        String temp = "(" + expression.replaceAll("~", "Q~") + ")";

        int rank = 0;
        for (int i = 0; i < temp.length(); i++) {
            if (Character.isLetter(temp.charAt(i))) {
                rank++;
            } else if (temp.charAt(i) != '(' && temp.charAt(i) != ')') {
                rank--;
            }

            if (rank != 0 && rank != 1) {
                return false;
            }
        }

        return rank == 1;
    }


    // EFFECTS: returns true if the brackets are the in the right position
    public boolean testCorrectOperatorsWithinBrackets() {
        Stack<Character> operators = new Stack<>();
        String temp = expression;
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) == ')') {
                if (!removeUntilBcktSuccessful(operators)) {
                    return false;
                }
            } else if (!Character.isLetter(temp.charAt(i))) {
                operators.push(temp.charAt(i));
            }
        }

        List<Character> chars = new ArrayList<>();
        chars.add('~');
        operators.removeAll(chars);

        if (operators.isEmpty()) {
            return true;
        } else if (!operators.contains('(')) {
            return checkAllEqual(operators);
        } else {
            return false;
        }
    }


    // MODIFIES: ops
    // EFFECTS: returns true if operators inside brackets are correct
    private boolean removeUntilBcktSuccessful(Stack<Character> ops) {
        List<Character> opList = new ArrayList<>();
        boolean isFound = false;
        boolean isAllEqual = true;
        while (!ops.empty()) {
            char curr = ops.pop();
            if (curr == '(') {
                isFound = true;
                if (!checkAllEqual(opList)) {
                    isAllEqual = false;
                }

                break;
            } else if (curr != '~') {
                opList.add(curr);
            }
        }

        return isAllEqual && isFound;
    }


    // EFFECTS: returns true if the sequence of operators is appropriate
    //          in particular, it doesn't violate associativity.
    private boolean checkAllEqual(List<Character> op) {
        if (op.isEmpty()) {
            return false;
        }

        if (op.size() == 1) {
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


    // EFFECTS: returns true if the logic expression contains no illegal characters
    public boolean hasNoIllegalChars() {
        for (char c : expression.toCharArray()) {
            if (!Character.isLetter(c) && !ALLOWED_OPS.contains(Character.toString(c))) {
                return false;
            }
        }

        return true;
    }


    // EFFECTS: returns a JSON representation of this logic expression
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("expression", expression);

        return json;
    }
}
