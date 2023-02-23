package ui;

import model.*;

public class TTGenerator {
    public static void main(String[] args) {
        AssignModel model = new AssignModel();
        model.add("P");
        model.add("Q");
        LogicExp exp = new LogicExp("(~(P=Q))");

        int i = 0;
        while (i < Math.pow(2, model.numOfSymbols())) {
            System.out.println(exp.evaluate(model));
            model.nextValues();

            i++;
        }

    }
}
