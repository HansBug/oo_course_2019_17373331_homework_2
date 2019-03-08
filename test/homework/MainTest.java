package homework;

import homework.poly.parse.PolyParser;
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
        assertExpWrongFormat("- + 123*x");
        assertExpDerEquals("4*x+x^2+x", "2*x+5");
        assertExpDerEquals("4*x+x^2+x", "4+2*x+1");
        assertExpWrongFormat("4x+x^2+x");
        assertExpDerEquals("- -4*x + x ^ 2 + x", "2*x+5");
        assertExpDerEquals("+4*x - -x^2 + x", "2*x+5");
        assertExpDerEquals("+19260817*x", "19260817");
        assertExpDerEquals("+ 19260817*x", "19260817");
        assertExpDerEquals("+ +19260817*x", "19260817");
        assertExpWrongFormat("++ 19260817*x");
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
        assertExpWrongFormat("++ 1*x^12");

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
    public void getLongerAns() {
        String longerInput = "- -4*x + x ^ 2 + x";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append(longerInput);
        }
        longerInput = builder.toString();
        assertExpDerEquals(longerInput, "2000*x+5000", false);

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

        // 38w+
        longerInput = "- -4*x + x ^ 2 + x + -0001 -+00000*x^0";
        builder = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            builder.append(longerInput);
        }
        longerInput = builder.toString();
        assertExpDerEquals(longerInput, "20000*x+50000", false);
    }

    private void assertExpDerEquals(
        String input, String expectedDer, boolean verbose) {
        if (!verbose) {
            System.out.println("verbose = " + verbose);
        }
        assertEquals(
            PolyParser.getInstance().parseExp(expectedDer, verbose).toString(),
            Main.getAns(input, verbose)
        );
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
        assertEquals("WRONG FORMAT!", Main.getAns(stream1));
        InputStream stream2 = new ByteArrayInputStream("\n".getBytes());
        assertEquals("WRONG FORMAT!", Main.getAns(stream2));
    }
}