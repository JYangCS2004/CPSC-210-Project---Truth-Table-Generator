package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentTest {
    Argument argument;
    LogicExp exp1, exp2, exp3, exp4;

    @BeforeEach
    void setUp() {
        argument = new Argument();
    }

    @Test
    void testConstructor() {
        assertTrue(argument.getExps().isEmpty());
        assertTrue(argument.getConclusion() == null);
    }

    @Test
    void addExpsTest() {
        exp1 = new LogicExp("~(P|Q)");
        exp2 = new LogicExp("R");
        exp3 = new LogicExp("P=Q");
        argument.addExp(exp1);
        argument.addExp(exp2);
        argument.addExp(exp3);

        argument.setConclusion(new LogicExp("P|R"));

        assertEquals(3, argument.getExps().size());

        argument.addExp(new LogicExp("P=R"));
        assertEquals(3, argument.getModel().numOfSymbols());
        assertEquals(4, argument.getExps().size());
    }

    @Test
    void deleteExpsTestDeleteOneFromModel() {
        argument.addExp(new LogicExp("P=R"));
        argument.addExp(new LogicExp("(R=Q)&S"));
        argument.setConclusion(new LogicExp("P"));

        argument.deleteExp(2);
        assertEquals(1, argument.getExps().size());
        assertEquals(2, argument.getModel().numOfSymbols());
        assertFalse(argument.getModel().isInSet("Q"));
        assertFalse(argument.getModel().isInSet("S"));

    }

    @Test
    void deleteExpsTestDeleteNoneFromModel() {
        argument.addExp(new LogicExp("P=R"));
        argument.addExp(new LogicExp("(R=Q)&S"));
        argument.setConclusion(new LogicExp("P"));

        argument.deleteExp(1);
        assertEquals(1, argument.getExps().size());
        assertEquals(4, argument.getModel().numOfSymbols());

    }
}
