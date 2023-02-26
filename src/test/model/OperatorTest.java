package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OperatorTest {
    Operator operator;
    Operator operator2;

    @BeforeEach
    void setUp() {
        operator = new Operator('|');
        operator2 = new Operator('&');
    }

    @Test
    void testConstructor() {
        assertEquals('|', operator.getOperator());
        assertEquals(0, operator.getRank());
        assertEquals(0, operator2.getRank());
    }

    @Test
    void evaluateTestOrOperator() {
        assertEquals(1, operator.evaluate(0, 1));
        assertEquals(0, operator.evaluate(0, 0));
        assertEquals(1, operator.evaluate(1, 1));
    }

    @Test
    void evaluateTestAndOperator() {
        assertEquals(1, operator2.evaluate(1, 1));
    }

    @Test
    void evaluateTestConditional() {
        Operator operatorCond = new Operator('>');
        assertEquals(1, operatorCond.evaluate(0, 1));
        assertEquals(0, operatorCond.evaluate(1, 0));
        assertEquals(0, operatorCond.getRank());
    }

    @Test
    void evaluateTestBicond() {
        Operator operatorBi = new Operator('=');
        assertEquals(1, operatorBi.evaluate(1, 1));
        assertEquals(0, operatorBi.evaluate(0, 1));
    }

    @Test
    void evaluateTestNegator() {
        Operator operatorNeg = new Operator('~');
        assertEquals(1, operatorNeg.getRank());
        assertEquals(1, operatorNeg.evaluate(0, 0));

        assertEquals(0, operatorNeg.evaluateNegator(1));
    }

    @Test
    void evaluateTestExOr() {
        Operator exOr = new Operator('*');
        assertEquals(0, exOr.evaluate(0, 0));
        assertEquals(1, exOr.evaluate(1, 0));
    }

    @Test
    void isHigherRankTestEqualRank() {
        Operator op = new Operator('|');
        assertFalse(op.isHigherRank('&'));
    }
}
