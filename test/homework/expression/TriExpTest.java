package homework.expression;

import homework.expression.parse.ExpParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TriExpTest {

    private void assertExpStrEquals(String input, String expected) {
        TriExp triExp =
            ((TriProdTermTree) ExpParser.getInstance().parseExp(input)
                .compute(new TriProdTermTree())).toTriExp();
        assertEquals(expected, triExp.toString());
    }

    @Test
    public void toStringTest() {
        assertExpStrEquals("-1+2*2*x*3", "12*x-1");
        assertExpStrEquals("-3* cos(x) ^2+1", "1-3*cos(x)^2");
        assertExpStrEquals("-3* cos(x) ^2 + 3*cos(x)\t^2", "0");
        assertExpStrEquals("0", "0");
        assertExpStrEquals("1", "1");
        assertExpStrEquals("-x", "-x");
        assertExpStrEquals("++sin(x)", "sin(x)");
        assertExpStrEquals("-+cos(x)", "-cos(x)");
    }
}