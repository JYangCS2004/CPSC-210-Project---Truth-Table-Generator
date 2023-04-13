package ui;


import model.Argument;
import model.EventLog;
import model.LogicExp;
import model.Event;
import persistence.ArgReader;
import persistence.ArgWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;


// GUI for Truth Table Generator App
public class TTGeneratorUI {
    private JFrame frame;
    private JPanel panel;
    private JList<String> list;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    private Argument argument;
    private JTextField addPrem;
    private JTextField addCon;
    private ArgReader reader;
    private ArgWriter writer;
    private static final String FILE = "./data/savedArgs.json";


    // starts the Truth Table Generator App
    public TTGeneratorUI() {
        argument = new Argument();
        panel = new JPanel();
        JPanel panelList = new JPanel(new BorderLayout());
        list = new JList<>();
        initializeDataPersistence();

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(list);
        list.setLayoutOrientation(JList.VERTICAL);
        panelList.add(scrollPane);

        frame = new JFrame();
        displayChoiceMenu();
        frame.setSize(450, 200);
        formatFrame(panelList, panel);
        argument = new Argument();
        frame.setResizable(false);
        frame.setVisible(true);

        initializeWindowListener();
    }


    // EFFECTS: initializes window listener
    public void initializeWindowListener() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event);
                }
            }
        });
    }


    // EFFECTS: displays the menu as a JPanel
    private void displayChoiceMenu() {
        JLabel adderLabel = new JLabel("  Add Premise: ");
        JLabel deleteLabel = new JLabel("Add Conclusion: ");
        initializeTextFields();

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ArgumentSaverActionListener());

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeletePremActionListener());

        JButton loadArg = new JButton("Load");
        loadArg.addActionListener(new ArgumentLoaderActionListener());

        JButton addButton = new JButton("Generate");
        addButton.addActionListener(new GenerateTableActionListener());

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        panel.add(adderLabel, setGridBagConstraints(c, 0, 0));
        panel.add(addPrem, setGridBagConstraints(c, 1, 0));
        panel.add(deleteLabel, setGridBagConstraints(c, 0, 1));
        panel.add(addCon, setGridBagConstraints(c, 1, 1));
        panel.add(saveButton, setGridBagConstraints(c, 0, 2));
        panel.add(deleteButton, setGridBagConstraints(c, 1, 2));
        panel.add(loadArg, setGridBagConstraints(c, 0, 3));
        panel.add(addButton, setGridBagConstraints(c, 1, 3));
        panel.setVisible(true);
    }


    // MODIFIES: c
    // EFFECTS: sets the positions for the grid, given the x and y positions
    private GridBagConstraints setGridBagConstraints(GridBagConstraints c, int x, int y) {
        c.gridx = x;
        c.gridy = y;
        return c;
    }

    // EFFECTS: initializes the reader and writer
    private void initializeDataPersistence() {
        reader = new ArgReader(FILE);
        writer = new ArgWriter(FILE);
    }

    // MODIFIES: this
    // EFFECTS: initializes the text fields
    private void initializeTextFields() {
        addPrem = new JTextField();
        addPrem.setBounds(50, 50, 100, 20);
        addPrem.addActionListener(new AddPremActionListener());

        addCon = new JTextField();
        addCon.setBounds(50, 110, 100, 20);
        addCon.addActionListener(new AddConActionListener());
    }

    // MODIFIES: this
    // EFFECTS: adds a new LogicExp based on the given input to the argument
    private void addToList(String text) {
        LogicExp testExp = new LogicExp(text);
        if (testExp.isValid()) {
            argument.addExp(testExp);
            listModel.addElement(testExp.getExpString());
            list.setModel(listModel);
        } else {
            displayErrorMessage();
        }
    }

    // EFFECTS: deletes object from argument, and corresponding jList
    private void deleteFromList(int index) {
        listModel.remove(index);
        argument.deleteExp(index + 1);
    }

    // MODIFIES: this
    // EFFECTS: formats the components and adds list and p to the frame
    private void formatFrame(JPanel list, JPanel p) {
        frame.setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Truth Table Generator");
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        title.setBackground(new Color(200, 200, 200));

        frame.add(title, BorderLayout.NORTH);
        frame.add(new JPanel(), BorderLayout.EAST);
        frame.add(new JPanel(), BorderLayout.SOUTH);
        frame.add(list, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEtchedBorder());
        frame.add(p, BorderLayout.WEST);
    }


    // ActionListener for adding a premise
    private class AddPremActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addPrem || e.getActionCommand().equals("Add")) {
                addToList(addPrem.getText());
            }
        }
    }


    // ActionListener for deleting a premise
    private class DeletePremActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Delete") && list.getSelectedIndex() != -1) {
                deleteFromList(list.getSelectedIndex());
            }
        }
    }

    // ActionListener for adding conclusion
    private class AddConActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addCon) {
                LogicExp con = new LogicExp(addCon.getText());
                if (con.isValid()) {
                    argument.setConclusion(con);
                    JOptionPane.showMessageDialog(null, "Conclusion added. :)");
                } else {
                    displayErrorMessage();
                }
            }
        }
    }


    // ActionListener for loading previously saved argument
    private class ArgumentLoaderActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                argument = reader.loadArgument();
                displayArgument();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Can't load previously saved argument... :(");
            }
        }
    }

    // ActionListener for generating table
    private class GenerateTableActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new ArgumentTable(argument);
            } catch (RuntimeException ex) {
                Image savedImg = new ImageIcon("./data/skull.png").getImage();
                Image newImg = savedImg.getScaledInstance(50, 60, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(null, "Argument incomplete. Couldn't load table.",
                        "", JOptionPane.WARNING_MESSAGE, new ImageIcon(newImg));
            }
        }
    }


    // ActionListener for saving argument
    private class ArgumentSaverActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                writer.open();
                writer.writeToFile(argument);
                writer.close();

                Image savedImg = new ImageIcon("./data/save-icon-36523.png").getImage();
                Image newImg = savedImg.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(null, "Argument Saved!",
                        "", JOptionPane.WARNING_MESSAGE, new ImageIcon(newImg));

            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Couldn't save file... :(");
            }
        }
    }


    // EFFECTS: displays the loaded argument to the screen
    private void displayArgument() {
        listModel.clear();
        for (LogicExp exps : argument.getExps()) {
            listModel.addElement(exps.getExpString());
        }

        if (argument.getConclusion() != null) {
            addCon.setText(argument.getConclusion().getExpString());
        }

        list.setModel(listModel);
    }


    // EFFECTS: displays a pop-up window notifying the user has inputted something invalid
    private void displayErrorMessage() {
        Image img = new ImageIcon("./data/sadface.png").getImage();
        Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        JOptionPane.showMessageDialog(null, "Invalid Input!",
                "Error!", JOptionPane.WARNING_MESSAGE, new ImageIcon(newImg));
    }

    public static void main(String[] args) {
        new TTGeneratorUI();
    }
}
