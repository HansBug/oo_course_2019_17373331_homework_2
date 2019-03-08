package homework.poly;

import homework.poly.parse.PolyParser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PolyExpTest {

    @Test
    public void toStringTest() {
        PolyExp exp = PolyParser.getInstance().parseExp(
            "0*x - -51 + 1*x^2 - 2*x^4 - +3* x ^ 4");
        PolyExp parsed = PolyParser.getInstance().parseExp(exp.toString());
        assertEquals(exp, parsed);

        exp = PolyParser.getInstance().parseExp(
            "-44760847419064553254302030225997824*    x^     2        " +
                "+1926801726265300013365358964864*x");
        parsed = PolyParser.getInstance().parseExp(exp.toString());
        assertEquals(exp, parsed);

        // -x+c
        assertExpStringEquals("-x+2", "2-x");

        // c*x^i
        assertExpStringEquals("21*x^345", "21*x^345");

        // c*x
        assertExpStringEquals("21*x^1", "21*x");

        // x^i
        assertExpStringEquals("1*x^-234", "x^-234");
        assertExpStringEquals("-1*x^234", "-x^234");

        // x
        assertExpStringEquals("1*x^1", "x");
        assertExpStringEquals("0+ -1*x^1", "-x");

        // c
        assertExpStringEquals("345*x^0+3", "348");
        assertExpStringEquals("x-x", "0");
    }

    private void assertExpStringEquals(String input, String expected) {
        PolyExp exp = PolyParser.getInstance().parseExp(input);
        assertEquals(expected, exp.toString());
    }
}