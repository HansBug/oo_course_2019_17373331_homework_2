package homework.tri;

import homework.expression.parse.ExpParser;
import org.junit.ComparisonFailure;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TriExpTest {

    private void assertExpStrEquals(String input, String expected) {
        TriExp triExp =
            ((TriProdTermTree) ExpParser.getInstance().parseExp(input)
                .compute(new TriProdTermTree())).toTriExp();
        assertEquals(expected, triExp.toString());
    }

    private void assertExpStrEquals(String input, String[] expected) {
        TriExp triExp =
            ((TriProdTermTree) ExpParser.getInstance().parseExp(input)
                .compute(new TriProdTermTree())).toTriExp();
        String triExpStr = triExp.toString();
        boolean flag = false;
        for (String exp :
            expected) {
            if (exp.equals(triExpStr)) {
                flag = true;
                break;
            }
        }
        assert flag;
    }

    @Test
    public void toStringTest() {
        assertExpStrEquals("-1+2*2*x*3", "12*x-1");
        try {
            assertExpStrEquals("-3* cos(x) ^2+1", "1-3*cos(x)^2");
        } catch (ComparisonFailure e) {
            assertExpStrEquals("-3* cos(x) ^2+1", "3*sin(x)^2-2");
        }
        assertExpStrEquals("-3* cos(x) ^2 + 3*cos(x)\t^2", "0");
        assertExpStrEquals("0", "0");
        assertExpStrEquals("1", "1");
        assertExpStrEquals("-x", "-x");
        assertExpStrEquals("++sin(x)", "sin(x)");
        assertExpStrEquals("-+cos(x)", "-cos(x)");
        assertExpStrEquals("+4*x - -x^2 + x", "5*x+x^2");
    }

    @Test
    public void mergeTrigo() {
        assertExpStrEquals("cos(x) * sin(x) ^2 + cos(x) ^3", "cos(x)");
        assertExpStrEquals("cos(x)^4 + 2 * sin(x) ^2 * cos(x) ^2 + sin(x) ^4",
            "1");
        assertExpStrEquals("sin(x) ^2 + cos(x) ^2", "1");
        assertExpStrEquals("sin(x)^3 + 3 * sin(x)^1*cos(x)^2",
            "3*sin(x)-2*sin(x)^3");
        assertExpStrEquals("1-cos(x)^2", "sin(x)^2");
        assertExpStrEquals("+x^2*cos(x)^3 - x^2*cos(x)",
            "-x^2*sin(x)^2*cos(x)");

        assertExpStrEquals("sin(x)^2-2*sin(x)^4+sin(x)^6",
            "sin(x)^2*cos(x)^4");

        try {
            assertExpStrEquals("sin(x)-sin(x)^3",
                "sin(x)*cos(x)^2");
        } catch (ComparisonFailure e) {
            assertExpStrEquals("sin(x)-sin(x)^3",
                "sin(x)-sin(x)^3");
        }

        assertExpStrEquals("sin(x)^5 + 2*sin(x)^3*cos(x)^2 + sin(x)*cos(x)^4",
            "sin(x)");
        assertExpStrEquals("2*sin(x)^3*cos(x)^2 + sin(x)^5 + sin(x)*cos(x)^4",
            "sin(x)");

        // index is -1
        assertExpStrEquals("3*sin(x)^-1*cos(x)+sin(x)*cos(x)^-1",
            "3*sin(x)^-1*cos(x)+sin(x)*cos(x)^-1");

        // index is -2
        assertExpStrEquals("sin(x)^-2+3",
            "3+sin(x)^-2");
        assertExpStrEquals("sin(x)^-4*cos(x)^-2+sin(x)^-2*cos(x)^-4",
            "sin(x)^-4*cos(x)^-4");
        assertExpStrEquals("2*sin(x)^-2 + 2*cos(x)^-2",
            "2*sin(x)^-2*cos(x)^-2");

        assertExpStrEquals("cos(x)^-4 + cos(x)^-2*sin(x)^-2",
            "sin(x)^-2*cos(x)^-4");
        assertExpStrEquals("sin(x)^-4 + sin(x)^-2*cos(x)^-2",
            "sin(x)^-4*cos(x)^-2");
        assertExpStrEquals("cos(x)^-4 + cos(x)^-2*sin(x)^-2 +" +
                "sin(x)^-4 + sin(x)^-2*cos(x)^-2",
            "sin(x)^-4*cos(x)^-4");
        assertExpStrEquals("cos(x)^-4 + sin(x)^-4 + 2*cos(x)^-2*sin(x)^-2",
            "sin(x)^-4*cos(x)^-4");

        assertExpStrEquals("x*sin(x)^2+cos(x)^2", "cos(x)^2+x*sin(x)^2");

    }

    @Test
    public void mergeTrigoSp() {
        assertExpStrEquals("sin(x)^2", "sin(x)^2");
        assertExpStrEquals("sin(x)^2*cos(x)^2+sin(x)^4", "sin(x)^2");
    }

    @Test
    public void mergeTrigo2() {
        try {

            assertExpStrEquals("sin(x)^4 + cos(x)^2*sin(x)^2 + cos(x)^4",
                "sin(x)^2+cos(x)^4");

        } catch (ComparisonFailure e1) {
            try {

                assertExpStrEquals("sin(x)^4 + cos(x)^2*sin(x)^2 + "
                        + "cos(x)^4",
                    "cos(x)^2+sin(x)^4");

            } catch (ComparisonFailure e) {
                try {

                    assertExpStrEquals("sin(x)^4 + cos(x)^2*sin(x)^2 + "
                            + "cos(x)^4",
                        "cos(x)^4+sin(x)^2");

                } catch (ComparisonFailure e2) {
                    System.out.println("Failed when expected both");
                    System.out.println("sin(x)^2+cos(x)^4");
                    System.out.println("and");
                    System.out.println("cos(x)^2+sin(x)^4");
                    throw e2;
                }
            }
        }
        assertExpStrEquals("cos(x)^4 + sin(x)^2*cos(x)^2 + sin(x)^4",
            new String[]{
                "sin(x)^2+cos(x)^4",
                "cos(x)^2+sin(x)^4",
                "cos(x)^4+sin(x)^2"});

    }
}