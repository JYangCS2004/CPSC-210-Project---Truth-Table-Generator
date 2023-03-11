package ui;

import model.*;
import persistence.ArgReader;
import persistence.ArgWriter;

import java.io.IOException;
import java.util.*;

public class TTGenerator {

    private Argument argument;
    private Scanner input;
    private ArgReader reader;
    private ArgWriter writer;
    private static final String FILE = "./data/savedArgs.json";


    // EFFECTS: Instantiates a new Truth Table Generator class
    public TTGenerator() {
        argument = new Argument();
        input = new Scanner(System.in);
        reader = new ArgReader(FILE);
        writer = new ArgWriter(FILE);

        runTTGenerator();
    }

    // EFFECTS: runs the Truth Table Generator
    public void runTTGenerator() {
        System.out.println("You currently have zero statements in your argument. Select of the options below:");
        displayMenu();
        while (true) {
            printEachExp();
            displayAppropriateMenu();
            String confirmed = getNextDecision();

            if (confirmed.equals("c")) {
                inputConclusion();
            } else if (confirmed.equals("g")) {
                printProperties();
                if (chooseToReturnToArgument()) {
                    break;
                }
            } else if (confirmed.equals("q")) {
                break;
            } else if (confirmed.equals("s")) {
                saveFile();
            } else if (confirmed.equals("l")) {
                loadPrevArgument();
            }
        }
    }


    // EFFECTS: displays the appropriate actions the user can take for next decision
    private void displayAppropriateMenu() {
        if (argument.getExps().size() == 0) {
            System.out.println("You have zero premises. Please enter a premise by entering 'a'.");
        } else {
            System.out.println("Here are your statements so far. Please select one of the options below:");
            displayMenu();
        }
    }


    // EFFECTS: display Menu (all possible user inputs)
    private void displayMenu() {
        System.out.println("\ta -> add a premise");
        System.out.println("\td -> delete a premise");
        System.out.println("\ts -> save the current argument to file");
        System.out.println("\tl -> load last saved argument");
        System.out.println("\tc -> change conclusion");
        canGenerateTable();
        System.out.println("\tq -> quit application");
    }


    // EFFECTS: prompts user to save the current argument
    private boolean chooseToReturnToArgument() {
        System.out.println("\n Truth Table successfully generated.");

        while (true) {
            System.out.println("\t Press '<' to return to argument.");
            System.out.println("\t Press 's' to save");
            System.out.println("\t Press 'q' to quit application");

            String choice = input.nextLine();
            if (choice.equals("q")) {
                return true;
            } else if (choice.equals("<")) {
                break;
            } else {
                saveFile();
            }
        }

        return false;
    }


    private void saveFile() {
        try {
            writer.open();
            writer.writeToFile(argument);
            writer.close();
            System.out.println("Argument saved.");
        } catch (IOException e) {
            System.out.println("Reeters. Can't save file. ");
        }
    }

    // MODIFIES: this
    // EFFECTS: get user's inputted expression. If the input is invalid, ask for input
    //         again until an appropriate input is received.
    private void getInputExp() {
        System.out.print("Please enter a premise on the next line:");
        while (true) {
            if (input.hasNextLine()) {
                LogicExp userExp = new LogicExp(input.nextLine());

                if (userExp.isValid()) {
                    argument.addExp(userExp);
                    break;
                }
            }

            System.out.println("Invalid input. Please try again...");
        }
    }



    // EFFECTS: prompts user to make a decision, and returns the decision made as a String
    private String getNextDecision() {
        String makeChoice;
        label:
        while (true) {
            if (input.hasNextLine()) {
                makeChoice = input.nextLine().toLowerCase();
                switch (makeChoice) {
                    case "c":
                    case "q":
                    case "g":
                    case "s":
                    case "l":
                        break label;
                    case "a":
                        getInputExp();
                        break label;
                    case "d":
                        if (askToDelete()) {
                            break label;
                        }
                }
            }
        }

        return makeChoice;
    }


    // EFFECTS: print properties of the current argument onto console
    private void printProperties() {
        printTruthTable();
        displayValidity();
        argument.getModel().reset();
    }



    // EFFECTS: loads the argument from previous session
    private void loadPrevArgument() {
        try {
            argument = reader.loadArgument();
        } catch (IOException e) {
            System.out.println("Can't load previous argument.");
        }
    }


    // EFFECTS: display each expression in the premises on a separate line
    private void printEachExp() {
        int num = 1;
        for (LogicExp exp : argument.getExps()) {
            System.out.println(num + "." + exp.getExpString());
            num++;
        }

        if (argument.getConclusion() != null) {
            System.out.println("-------------- \n"
                    + argument.getConclusion().getExpString());
        }
    }


    // EFFECTS: prompt user to select a premise to remove, by entering a number
    private boolean askToDelete() {
        if (argument.getExps().isEmpty()) {
            System.out.println("Can't delete more statements.");
            return false;
        }
        System.out.println("I see you have chosen to remove a statement. "
                + "Select the one to be deleted by entering the corresponding number"
                + " (e.g., to delete " + argument.getExps().get(0).getExpString() + ", input 1).");

        while (true) {
            try {
                int deleteNum = input.nextInt();

                if (deleteNum <= argument.getExps().size()) {
                    argument.deleteExp(deleteNum);
                    break;
                } else {
                    System.out.println("Invalid input. Please try again...");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please try again...");
                input.next();
            }
        }

        return true;
    }

    // EFFECTS: prompts user to return to argument, or to quit the application
    private boolean returnToArgument() {
        if (input.hasNextLine()) {
            String choice = input.nextLine();
            return choice.equals("<");
        }

        return false;
    }

    // EFFECTS: prompt user to input a conclusion statement
    private void inputConclusion() {
        System.out.print("Please input a concluding expression:");
        while (true) {
            if (input.hasNextLine()) {
                LogicExp userExp = new LogicExp(input.nextLine());

                if (userExp.isValid()) {
                    argument.setConclusion(userExp);
                    break;
                }
            }

            System.out.println("Invalid input. Please try again...");
        }
    }


    // EFFECTS: prints to console saying whether it is possible to generate the table
    private void canGenerateTable() {
        if (argument.getExps().size() != 0 && argument.getConclusion() != null) {
            System.out.println("\tg -> generate table");
        }
    }

    // EFFECTS: prints out the truth table based on the given premises and conclusion
    private void printTruthTable() {
        List<String> headerRow = new ArrayList<>(argument.getModel().getSymbols());

        for (LogicExp e : argument.getExps()) {
            headerRow.add(e.getExpString());
        }

        headerRow.add(argument.getConclusion().getExpString());

        String format = getFormatting(headerRow);

        System.out.println(String.format(format, headerRow.toArray()) + "\n");

        for (int i = 0; i < Math.pow(2, argument.getModel().numOfSymbols()); i++) {
            List<Integer> numRow = new ArrayList<>();
            numRow.addAll(argument.getModel().getValues());
            numRow.addAll(argument.computeEach());

            System.out.println(String.format(format, numRow.toArray()) + "\n");
            argument.getModel().nextValues();
        }
    }


    // EFFECTS: returns the formatting of the table
    private String getFormatting(List<String> header) {
        StringBuilder formatBuild = new StringBuilder();

        for (int n = 0; n < header.size(); n++) {
            formatBuild.append("%-").append(longestExp() + 4).append("s");
        }

        return formatBuild.toString();
    }


    // EFFECTS: tells the user if the argument is valid
    private void displayValidity() {
        System.out.println("\n Validity: ");
        AssignModel possibleModel = argument.returnInvalidModel();
        if (possibleModel != null) {
            System.out.println("The argument is invalid.");

            for (String s : possibleModel.getSymbols()) {
                System.out.print(s + ":" + possibleModel.getValForSymbol(s) + "    ");
            }

        } else {
            System.out.println("The argument is valid.");
        }
    }


    // EFFECTS: returns the length of the longest expression
    private int longestExp() {
        int longestLength = argument.getExps().get(0).getExpString().length();

        for (LogicExp e : argument.getExps()) {
            longestLength = Math.max(longestLength, e.getExpString().length());
        }

        return longestLength;
    }
}
