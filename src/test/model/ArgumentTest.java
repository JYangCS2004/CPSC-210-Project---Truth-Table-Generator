package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ArgumentTest {
    Argument argument;
    LogicExp exp1, exp2, exp3;

    @BeforeEach
    void setUp() {
        argument = new Argument();

        exp1 = new LogicExp("~(P|Q)");
        exp2 = new LogicExp("R");
        exp3 = new LogicExp("P=Q");
        argument.addExp(exp1);
        argument.addExp(exp2);
        argument.addExp(exp3);

        argument.changeConclusion("P");
    }

    @Test
    void testConstructor() {
        List<LogicExp> exps = new ArrayList<>();
        exps.add(exp1);
        exps.add(exp2);
        exps.add(exp3);
    }
}
