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
        exp = new LogicExp("P  | Q");
        longerExp = new LogicExp("(P|Q)=R");
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
        LogicExp expBracket = new LogicExp("~(P&Q)&(P|Q)");
        AssignModel model = new AssignModel();
        model.add("P");
        model.add("Q");

        model.nextValues();
        model.nextValues();
        assertEquals(1, expBracket.evaluate(model));
        assertEquals(1, new LogicExp("(P|~(P&Q))").evaluate(model));
    }

    @Test
    void isValidTestValidExps() {
        assertTrue(new LogicExp("P&Q").testCorrectOperatorsWithinBrackets());
        assertTrue(new LogicExp("(P&Q)").testCorrectOperatorsWithinBrackets());
        assertFalse(new LogicExp("()P()&Q").testCorrectOperatorsWithinBrackets());
        assertTrue(new LogicExp("(P|Q|P)").testCorrectOperatorsWithinBrackets());
        assertTrue(new LogicExp("P&~P").isValid());

    }

    @Test
    void isValidTestMissingBrackets() {
        LogicExp brackets = new LogicExp("((P|Q");
        LogicExp brackets1 = new LogicExp("(~P&D)|(D&D))");

        assertFalse(brackets.isValid());
        assertFalse(brackets1.isValid());
    }

    @Test
    void isValidTestTooManyOfEach() {
        assertFalse(new LogicExp("(=PPQ|").isValid());
        assertFalse(new LogicExp("~~P|~~~|||||Q").isValid());
        assertFalse(new LogicExp("P=Q&Q&").isValid());
    }

    @Test
    void isValidTestIllegalSymbols() {
        assertFalse(new LogicExp("P$Q").isValid());
        assertFalse(new LogicExp("{P|Q}").isValid());
    }

    @Test
    void isValidTestViolateAssoc() {
        assertFalse(new LogicExp("(P=Q=R)").isValid());
        assertFalse(new LogicExp("P&R=A").isValid());
    }

}