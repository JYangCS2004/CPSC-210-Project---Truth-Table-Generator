package persistence;

import model.Argument;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ArgReaderTest {
    @Test
    void loadArgumentTestNonExistentFile() {
        try {
            ArgReader reader = new ArgReader("./data/SusFile.json");
            Argument arg = reader.loadArgument();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void loadArgumentTestGeneralArg() {
        ArgReader reader = new ArgReader("./data/reeters.json");
        try {
            Argument newArg = reader.loadArgument();
            assertEquals(2, newArg.getExps().size());
            assertEquals("P=Q", newArg.getConclusion().getExpString());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }
}
