package persistence;

import model.Argument;
import model.LogicExp;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArgWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Argument arg = new Argument();
            ArgWriter argWriter = new ArgWriter("./data/*.json");
            argWriter.open();
            fail();
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterGeneralArgument() {
        try {
            Argument argument = new Argument();
            argument.addExp(new LogicExp("P"));
            argument.addExp(new LogicExp("P=Q"));
            argument.setConclusion(new LogicExp("Q"));

            ArgWriter writer = new ArgWriter("./data/testFile.json");
            writer.open();
            writer.writeToFile(argument);
            writer.close();

            ArgReader reader = new ArgReader("./data/testFile.json");
            Argument copied = reader.loadArgument();
            List<LogicExp> premises = copied.getExps();
            assertEquals("P", premises.get(0).getExpString());
            assertEquals("P=Q", premises.get(1).getExpString());
            assertEquals("Q", copied.getConclusion().getExpString());

        } catch (IOException e) {
            fail("Exception not expected.");
        }
    }

    @Test
    void testWriterArgumentWithNoConclusion() {
        try {
            Argument argNoCon = new Argument();
            argNoCon.addExp(new LogicExp("~P"));
            argNoCon.addExp(new LogicExp("~P&~Q"));

            ArgWriter writer = new ArgWriter("./data/testNoConclusion.json");
            writer.open();
            writer.writeToFile(argNoCon);
            writer.close();

            ArgReader reader = new ArgReader("./data/testNoConclusion.json");
            Argument copied2 = reader.loadArgument();
            List<LogicExp> premises = copied2.getExps();
            assertEquals("~P", premises.get(0).getExpString());
            assertEquals("~P&~Q", premises.get(1).getExpString());
            assertNull(copied2.getConclusion());

        } catch (IOException e) {
            fail("Exception not expected.");
        }
    }
}
