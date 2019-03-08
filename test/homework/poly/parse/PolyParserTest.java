package homework.poly.parse;

import homework.poly.PolyExp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PolyParserTest {
    private void assertExpEqualsTerm(String input, int coe, int ind) {
        try {
            assertEquals(
                new PolyExp(ind, coe),
                PolyParser.getInstance().parseExp(input, true)
            );
        } catch (AssertionError e) {
            System.out.println("Test failed.");
            System.out.println("Input = " + input);
            throw e;
        }
    }

    private void assertExpIsNull(String input) {
        try {
            assertNull(PolyParser.getInstance().parseExp(input, true));
        } catch (AssertionError e) {
            System.out.println("Test failed.");
            System.out.println("Input = " + input);
            throw e;
        }
    }

    private void assertExpEqualsExp(String input, int[] coes, int[] inds) {
        assert coes.length == inds.length;
        PolyExp res = PolyParser.getInstance().parseExp(input, true);
        PolyExp exp = new PolyExp();
        for (int i = 0; i < coes.length; i++) {
            exp.add(new PolyExp(inds[i], coes[i]));
        }
        assertEquals(exp, res);
    }

    @org.junit.Test
    public void parseExp() {
        assertExpEqualsTerm("1", 1, 0);
        assertExpEqualsTerm("-1", -1, 0);
        assertExpEqualsTerm("--1", 1, 0);
        assertExpEqualsTerm("x", 1, 1);
        assertExpEqualsTerm("-x", -1, 1);
        assertExpEqualsTerm("2*x", 2, 1);
        assertExpEqualsTerm("x^-1", 1, -1);
        assertExpEqualsTerm("2*x^-1", 2, -1);
        assertExpEqualsTerm("1234*x^-5678", 1234, -5678);
        assertExpEqualsTerm("-+1234*  x ^  -5678", -1234, -5678);
        assertExpEqualsTerm(" + + x ^ 2", 1, 2);
        assertExpIsNull("");
        assertExpIsNull("   ");
        assertExpIsNull("\t");
        assertExpIsNull("4*x+x2+x");
        assertExpIsNull("0*x -- 51 + 2*x^2 ");
        assertExpEqualsExp("4*x+x^2+x",
            new int[]{4, 1, 1}, new int[]{1, 2, 1});
        assertExpEqualsExp(
            "-447608474*    x^     2        " +
                "+1926801726*x",
            new int[]{1926801726, -447608474},
            new int[]{1, 2});
    }
}
