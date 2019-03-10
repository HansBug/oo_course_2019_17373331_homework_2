package homework;

import homework.expression.core.ComputableTester;
import homework.expression.core.Expression;
import homework.expression.parse.ExpParser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void getAns() {
        assertExpDerEquals("0", "0");
        assertExpDerEquals("1", "0");
        assertExpDerEquals("x", "1");
        assertExpDerEquals("+x", "1");
        assertExpDerEquals("-x", "-1");
        assertExpDerEquals("234*x", "234");
        assertExpDerEquals("++123*x", "123");
        assertExpDerEquals("-  +123*x", "-123");
        assertExpDerEquals("- + 123*x", "-123"); // - (+1 * 123* x)

        // examples
        assertExpDerEquals("4*x+x^2+x", "2*x+5");
        assertExpDerEquals("4*x+x^2+x", "4+2*x+1");
        assertExpWrongFormat("4x+x^2+x");
        assertExpDerEquals("- -4*x + x ^ 2 + x", "2*x+5");
        assertExpDerEquals("+4*x - -x^2 + x", "2*x+5");
        assertExpDerEquals("+19260817*x", "19260817");
        assertExpDerEquals("+ 19260817*x", "19260817");
        assertExpDerEquals("+ +19260817*x", "19260817");
        assertExpDerEquals("++ 19260817*x", "19260817");
        assertExpWrongFormat("1926 0817 * x");
        assertExpWrongFormat("");

        assertExpDerEquals(
            "1234322340987832+x+     1234322340987832-x -           " +
                "-78978678978977687      -x-x", "-2");

        // fucking zero
        assertExpDerEquals("+0000050*x^000012", "--00600*x^+00011");

        // fucking fucked radix
        assertExpWrongFormat("0x123*x");
        assertExpWrongFormat("123F*x");
        assertExpWrongFormat("123f*x");
        assertExpWrongFormat("123o*x");
        assertExpWrongFormat("12_3*x");

        // wrong character
        assertExpWrongFormat("1*a^2");
        assertExpWrongFormat("1.1*x^23");
        assertExpWrongFormat("1/2*x");

        // empty output
        assertExpDerEquals("++ x+ - x ", "0");

        // wrong blank
        assertExpWrongFormat("1 233*x");
        assertExpWrongFormat("12*x^+ 12");
        assertExpWrongFormat("12*x^ +1 2");
        assertExpWrongFormat("2*x^ - 1");
        assertExpDerEquals("+ 1*x^12 ", "12*x^11");
        assertExpDerEquals("+\t1\t*x\t^12\t", "12*x^11");
        assertExpDerEquals("+\t 1 \t*x \t \t^12\t", "12*x^11");
        assertExpWrongFormat("\f1"); // but i don't think it's useful.
        assertExpDerEquals("++ 1*x^12", "12*x^11");

        //wrong term
        assertExpWrongFormat("        xx^2");
        assertExpWrongFormat("x^^2");
        assertExpWrongFormat("++-x^2");
        assertExpWrongFormat("x^++2");
        assertExpWrongFormat("1x^3");
        assertExpWrongFormat("1*x^+x");
        assertExpWrongFormat("*x");

        // positive
        assertExpDerEquals("-  4*  x^  0-x  ^  -529920192",
            "529920192*x^-529920193");
        assertExpDerEquals("0*x+3", "0");
        assertExpDerEquals("0 * x^1 + x^0 + 4 * x ^ 2 - -5*x", "8*x+5");
        assertExpDerEquals("5*x+6*x^1-x^1+00000*x^0+000010*x^+1",
            "5+6-1+10");
        assertExpDerEquals("4 * x ^-3 + 2 * x ^ -1 + 01 * x ^ 0 - 1 * x ^ 1",
            "-12*x^-4+-2*x^-2+0-1");
    }

    @Test
    public void getAnsNewExamplesTest() {
        //12
        assertExpDerEquals("2*sin(x)", "2*cos(x)");
        //13
        assertExpDerEquals("-2*cos(x)", "2*sin(x)");
        //14
        assertExpWrongFormat("5*sin(2 * x)");
        //15
        assertExpWrongFormat("8*cos(x^2)");
        //16
        assertExpWrongFormat("3*sin(90)");
        //17
        assertExpDerEquals("23+sin(x)*3+x^8", "3*cos(x)+8*x^7");
        //18
        assertExpDerEquals("cos(x)* sin(x)* 5+4 *x^3",
            "-5*sin(x)^2+5*cos(x)*cos(x) + 12*x^2");
        //19
        assertExpDerEquals("43+4*x^3", "12*x^2");
        //20
        assertExpDerEquals("5* x^4* sin(x)",
            "5*x^4*cos(x)+20*x^3*sin(x)");
        //21
        assertExpDerEquals("5*x^4*cos(x)",
            "20*x^3*cos(x)-5*x^4*sin(x)");
        //22
        assertExpWrongFormat("6*si n(x)");
        //23
        assertExpWrongFormat("6*co s(x)");
        //24
        assertExpDerEquals("2*x^2*3", "12*x");
    }

    @Test
    public void getAnsNew() {
        assertExpDerEquals("cos(x) ^2 + sin(x) ^2", "0");
        assertExpDerEquals("x * x*\tx*x*x-0*x", "5*x^4");
        assertExpDerEquals("++x", "1");
        assertExpWrongFormat("+++x");
        assertExpWrongFormat("++++x");
        assertExpDerEquals("+++1", "0");
        assertExpDerEquals("++ +1", "0");
        assertExpDerEquals("+ + +1", "0");
        assertExpWrongFormat("+ + + 1");

        assertExpDerEquals("+ +x* 1-+ 1*x", "0");
        assertExpDerEquals("+++19260817", "0");
        assertExpWrongFormat("2*+sin(x)+4*cos(x)");
        assertExpDerEquals("cos (x) + sin (x)", "-sin(x)+cos(x)");
        assertExpDerEquals("+ ++0*x^2*sin (x)^3", "0");
    }

    @Test
    public void getLongerAns() {
        String longerInput = "- -4*x + x ^ 2 + x";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append(longerInput);
        }
        longerInput = builder.toString();
        assertExpDerEquals(longerInput, "2000*x+5000", false);
    }

    @Test
    public void getLongerAns2() {
        assertExpDerEquals("+x^1+ x^1 +x^1 + x^1++x^1+ +x^1++ x^1 ++x^1+ +" +
                " x^1 ++ x^1 + +x^1 + + x^1-x^1- x^1 -x^1 - " +
                "x^1--x^1- -x^1-- x^1 --x^1- - x^1 -- x^1 - -x^1 - - x^" +
                "1+-x^1+ -x^1+- x^1 +-x^1+ - x^1 +- x^1 + -x^1 + - x^1-+" +
                "x^1- +x^1-+ x^1 -+x^1- + x^1 -+ x^1 - +x^1 - + x^1+x ^ 1+ " +
                "x ^ 1 +x ^ 1 + x ^ 1++x ^ 1+ +x ^ 1++ x ^ 1 ++x ^ 1+ +" +
                " x ^ 1 ++ x ^ 1 + +x ^ 1 + + x ^ 1-x ^ 1- x ^ 1 -x ^ 1 - x " +
                "^ 1--x ^ 1- -x ^ 1-- x ^ 1 --x ^ 1- - x ^ 1 -- x ^ 1 - -x" +
                " ^ 1 - - x ^ 1+-x ^ 1+ -x ^ 1+- x ^ 1 +-x ^ 1+ - x ^ 1 " +
                "+- x ^ 1 + -x ^ 1 + - x ^ 1-+x ^ 1- +x ^ 1-+ x ^ 1 -+x ^ " +
                "1- + x ^ 1 -+ x ^ 1 - +x ^ 1 - + x ^ 1+x ^1+ x ^1 +x ^1 +" +
                " x ^1++x ^1+ +x ^1++ x ^1 ++x ^1+ + x ^1 ++ x ^1 + +x ^1 + +" +
                " x ^1-x ^1- x ^1 -x ^1 - x ^1--x ^1- -x ^1-- x ^1 --x ^1-" +
                " - x ^1 -- x ^1 - -x ^1 - - x ^1+-x ^1+ -x ^1+- x ^1 " +
                "+-x ^1+ - x ^1 +- x ^1 + -x ^1 + - x ^1-+x ^1- +x ^1-+" +
                " x ^1 -+x ^1- + x ^1 -+ x ^1 - +x ^1 - + x ^1+x^ 1+ x^ 1" +
                " +x^ 1 + x^ 1++x^ 1+ +x^ 1++ x^ 1 ++x^ 1+ + x^ 1 ++ x^ 1 +" +
                " +x^ 1 + + x^ 1-x^ 1- x^ 1 -x^ 1 - x^ 1--x^ 1- -x^ 1--" +
                " x^ 1 --x^ 1 ",
            "12", false);
    }

    public void getLongerLongerAns() {
        // 38w+
        String longerInput = "- -4*x + x ^ 2 + x + -0001 -+00000*x^0";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            builder.append(longerInput);
        }
        longerInput = builder.toString();
        assertExpDerEquals(longerInput, "20000*x+50000", false);
    }

    private void assertExpDerEquals(
        String input, String expectedDer, boolean verbose) {
        System.out.println("====================================");
        System.out.println("MainTest.assertExpDerEquals");
        if (!verbose) {
            System.out.println("verbose = " + verbose);
        }

        if (verbose) {
            System.out.println("-------------------");
            System.out.println("parsing expected exp");
        }
        Expression expectedExp = ExpParser.getInstance()
            .parseExp(expectedDer, verbose);

        if (verbose) {
            System.out.println("expectedExp = " + expectedExp);
        }

        if (verbose) {
            System.out.println("-------------------");
            System.out.println("parsing actual exp");
        }
        Expression actualExp = ExpParser.getInstance().
            parseExp(Main.getAns(input, verbose));
        if (verbose) {
            System.out.println("actualExp = " + actualExp);
        }
        ComputableTester tester = new ComputableTester(97);
        ComputableTester expected =
            (ComputableTester) expectedExp.compute(tester);
        ComputableTester actual =
            (ComputableTester) actualExp.compute(tester);

        try {
            assertEquals(expected, actual);
        } catch (AssertionError e) {
            System.out.println("---------------");
            System.out.println("wrong:");
            System.out.println("expected = " + expected);
            System.out.println("actual = " + actual);
            throw e;
        }
    }

    private void assertExpDerEquals(String input, String expectedDer) {
        assertExpDerEquals(input, expectedDer, true);
    }

    private void assertExpWrongFormat(String input) {
        assertEquals("WRONG FORMAT!", Main.getAns(input, true));
    }

    @Test
    public void getAnsByStream() {
        InputStream stream1 = new ByteArrayInputStream("".getBytes());
        assertEquals("WRONG FORMAT!", Main.getAns(stream1, true));
        InputStream stream2 = new ByteArrayInputStream("\n".getBytes());
        assertEquals("WRONG FORMAT!", Main.getAns(stream2, true));
    }
}