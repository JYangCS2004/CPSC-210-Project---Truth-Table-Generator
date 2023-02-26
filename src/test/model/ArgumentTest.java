package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentTest {
    Argument argument1;
    Argument arg2;
    LogicExp exp1, exp2, exp3, exp4;

    @BeforeEach
    void setUp() {
        argument1 = new Argument();
        arg2 = new Argument();
        arg2.addExp(new LogicExp("P=R"));
        arg2.addExp(new LogicExp("(R=Q)&S"));
        arg2.setConclusion(new LogicExp("P"));
    }

    @Test
    void testConstructor() {
        assertTrue(argument1.getExps().isEmpty());
        assertTrue(argument1.getConclusion() == null);
    }

    @Test
    void addExpsTest() {
        exp1 = new LogicExp("~(P|Q)");
        exp2 = new LogicExp("R");
        exp3 = new LogicExp("P=Q");
        argument1.addExp(exp1);
        argument1.addExp(exp2);
        argument1.addExp(exp3);

        argument1.setConclusion(new LogicExp("P|R"));

        assertEquals(3, argument1.getExps().size());

        argument1.addExp(new LogicExp("P=R"));
        assertEquals(3, argument1.getModel().numOfSymbols());
        assertEquals(4, argument1.getExps().size());
    }

    @Test
    void setConclusionAddNewSymbols() {
        arg2.setConclusion(new LogicExp("A"));
        assertEquals(5, arg2.getModel().numOfSymbols());
        assertTrue(arg2.getModel().isInSet("A"));
    }

    @Test
    void deleteExpsTestDeleteOneFromModel() {
        arg2.deleteExp(2);
        assertEquals(1, arg2.getExps().size());
        assertEquals(2, arg2.getModel().numOfSymbols());
        assertFalse(arg2.getModel().isInSet("Q"));
        assertFalse(arg2.getModel().isInSet("S"));

    }

    @Test
    void deleteExpsTestDeleteNoneFromModel() {
        arg2.deleteExp(1);
        assertEquals(1, arg2.getExps().size());
        assertEquals(4, arg2.getModel().numOfSymbols());
    }

    @Test
    void computeEachTest() {
        List<Integer> results = new ArrayList<>();
        results.add(1);
        results.add(0);
        results.add(0);
        assertEquals(results, arg2.computeEach());
    }

    @Test
    void returnInvalidModelTest() {
        argument1.addExp(new LogicExp("Q"));
        argument1.addExp(new LogicExp("P>Q"));
        argument1.setConclusion(new LogicExp("P"));

        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(0);
        assertEquals(values, argument1.returnInvalidModel().getValues());
    }

    @Test
    void returnInvalidModelTestValid() {
        argument1.addExp(new LogicExp("P"));
        argument1.addExp(new LogicExp("P>Q"));
        argument1.setConclusion(new LogicExp("Q"));

        assertNull(argument1.returnInvalidModel());
    }

    @Test
    void deleteExpsTestNoConclusion() {
        Argument arg3 = new Argument();
        arg3.addExp(new LogicExp("P*Q"));
        arg3.addExp(new LogicExp("R"));

        arg3.deleteExp(2);
        assertEquals(2, arg3.getModel().numOfSymbols());
    }
}
