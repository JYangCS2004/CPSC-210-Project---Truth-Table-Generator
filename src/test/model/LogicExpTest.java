package model;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogicExpTest {
    LogicExp exp;
    LogicExp longerExp;

    @BeforeEach
    void setUp() {
        exp = new LogicExp("(P|Q)");
        longerExp = new LogicExp("((P|Q)=R)");
    }

    @Test
    void testConstructor() {
        assertEquals("P|Q", exp.getExpString());

        List<String> usedSymbols = new ArrayList<>();
        usedSymbols.add("P");
        usedSymbols.add("Q");
        assertEquals(usedSymbols, exp.getSymbolsUsed());
    }

    @Test
    void evaluateTest() {
        AssignModel newModel = new AssignModel();
        newModel.add("P");
        newModel.add("Q");
        assertEquals(0, exp.evaluate(newModel));

        newModel.nextValues();
        assertEquals(1, exp.evaluate(newModel));
    }

    @Test
    void evaluateTestMultipleBrackets() {
        AssignModel model = new AssignModel();
        model.add("P");
        model.add("Q");
        model.add("R");

        model.nextValues();
        model.nextValues();
        assertEquals(0, longerExp.evaluate(model));

    }
}