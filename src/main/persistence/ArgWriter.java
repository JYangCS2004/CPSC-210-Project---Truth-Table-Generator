package persistence;

import model.Argument;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

// code adapted from CPSC210/JsonSerializationDemo
// JSON Writer for argument
public class ArgWriter {
    private PrintWriter writer;
    private String destFile;

    // REQUIRES: destFile must exist
    // EFFECTS: constructs a new argument writer
    public ArgWriter(String destFile) {
        this.destFile = destFile;
    }

    // MODIFIES: this
    // EFFECTS: opens writer for writing to destination file
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destFile);
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of the given argument to file
    public void writeToFile(Argument arg) {
        JSONObject json = arg.toJson();
        writer.print(json);
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }
}
