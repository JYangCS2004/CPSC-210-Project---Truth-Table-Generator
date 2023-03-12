package persistence;

import model.Argument;
import model.LogicExp;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;


// code adapted from CPSC210/JsonSerializationDemo
// JSON Reader for argument
public class ArgReader {
    private String source;

    // EFFECTS: constructs a reader that reads argument from file
    public ArgReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads the JSON file and returns the argument the JSON object represents
    //        throws IOException if unable to read from file
    public Argument loadArgument() throws IOException {
        Argument arg = new Argument();
        JSONObject json = readFile();

        JSONArray jsonArr = json.getJSONArray("premises");
        addPremises(arg, jsonArr);
        addConclusion(arg, json);

        return arg;
    }


    // EFFECTS: adds premises to the given argument
    private void addPremises(Argument arg, JSONArray jsonArr) {
        for (Object o : jsonArr) {
            JSONObject j = (JSONObject) o;
            arg.addExp(new LogicExp(j.getString("expression")));
        }
    }


    // EFFECTS: adds the given conclusion to the argument
    private void addConclusion(Argument arg, JSONObject json) {
        JSONObject conclusionObj = json.getJSONObject("conclusion");

        if (conclusionObj.length() != 0) {
            String exp = conclusionObj.getString("expression");
            arg.setConclusion(new LogicExp(exp));
        }
    }

    // EFFECTS: retrieves content from json file and returns the JSONObject from it
    //        throws IOException if the file does not exist
    private JSONObject readFile() throws IOException {
        return new JSONObject(new String(Files.readAllBytes(Paths.get(source))));
    }
}
