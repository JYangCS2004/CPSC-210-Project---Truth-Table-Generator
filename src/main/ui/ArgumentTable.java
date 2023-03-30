package ui;

import model.Argument;
import model.AssignModel;
import model.LogicExp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArgumentTable {
    private JFrame frame;
    private JTable table;
    private Argument argument;

    public ArgumentTable(Argument arg) {
        frame = new JFrame();
        argument = arg;
        displayError();
        List<Integer[]> list = new ArrayList<>();
        int maxRows = (int) Math.pow(2, argument.getModel().numOfSymbols());

        for (int i = 0; i < maxRows; i++) {
            list.add(generateEachRow(arg.computeEach()));
            argument.getModel().nextValues();
        }

        Integer[][] data = list.toArray(new Integer[0][]);

        table = new JTable(data, getNames());
        table.setBounds(30, 30, 200, 300);
        table.setDefaultEditor(Object.class, null);

        JScrollPane sp = new JScrollPane(table);
        frame.add(sp);
        displayInvalidModel();
        frame.setSize(500, 200);
        frame.setVisible(true);
    }

    private Integer[] generateEachRow(List<Integer> computed) {
        List<Integer> eachValue = new ArrayList<>(argument.getModel().getValues());
        eachValue.addAll(computed);
        return eachValue.toArray(new Integer[0]);
    }

    private String[] getNames() {
        List<String> names = new ArrayList<>(argument.getModel().getSymbols());

        for (LogicExp exp : argument.getExps()) {
            names.add(exp.getExpString());
        }

        names.add(argument.getConclusion().getExpString());

        return names.toArray(names.toArray(new String[0]));
    }


    // EFFECTS: display a JOptionPane if the argument is incomplete
    private void displayError() {
        if (argument.getConclusion() == null || argument.getExps().size() == 0) {
            throw new RuntimeException();
        }
    }


    private void displayInvalidModel() {
        argument.getModel().reset();
        JPanel panel = new JPanel();
        AssignModel model = argument.returnInvalidModel();

        StringBuilder invalidModel = new StringBuilder();

        if (model != null) {
            for (String s : model.getSymbols()) {
                invalidModel.append(" ").append(s).append(":").append(model.getValForSymbol(s));
            }
        } else {
            invalidModel.append("N\\A");
        }

        JLabel label = new JLabel("Invalid Model: " + invalidModel);
        label.setFont(new Font("Serif", Font.PLAIN, 20));
        panel.add(label);
        frame.add(panel, BorderLayout.SOUTH);

        argument.getModel().reset();
    }
}
