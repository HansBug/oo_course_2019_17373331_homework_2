package homework.expression.parse;

import homework.expression.core.DefaultExpFactory;
import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpParser {

    private static final ExpFactory FACTORY = DefaultExpFactory.getInstance();

    public static ExpParser getInstance() {
        return ExpParserSingleton.singleton;
    }

    /**
     * Parse a PolyExp from giving string
     *
     * @param input just a string
     * @return parsed expression. if failed, return null
     */
    public Expression parseExp(String input) {
        return parseExp(input, false);
    }

    public Expression parseExp(String input, boolean debug) {
        String buffer;
        TermParser parser = TermParser.getInstance();
        Expression resExp = FACTORY.constant(0);
        buffer = preprocessInput(input);
        final String pmStr = "^[ \\t]*([-+])[ \\t]*";
        Pattern pmPtn = Pattern.compile(pmStr);
        while (!buffer.isEmpty()) {
            Matcher starMarcher = pmPtn.matcher(buffer);
            boolean isPos = true;
            if (starMarcher.find()) {
                buffer = buffer.substring(starMarcher.end());
                if ("-".equals(starMarcher.group(1))) {
                    isPos = false;
                }
                if (debug) {
                    System.out.println("isPos = " + isPos);
                }
            } else {
                return null;
            }
            Expression exp = parser.parseTermExp(buffer, debug);
            if (exp != null) {
                if (isPos) {
                    resExp = resExp.add(exp);
                } else {
                    resExp = resExp.add(exp.negate());
                }
                buffer = parser.getRemainedBuffer();
            } else {
                return null;
            }
            if (debug) {
                System.out.println("current resExp = " + resExp);
            }
        }
        return resExp;
    }

    /**
     * Check & add a '+' at the head of giving string.
     *
     * @param input just input
     * @return a string whose first visible character is '+' or '-
     */
    private String preprocessInput(String input) {
        Pattern headSignPattern = Pattern.compile("^[ \\t]*[-+]");
        if (!headSignPattern.matcher(input).find()) {
            return "+" + input;
        } else {
            return input;
        }
    }

    /**
     * Singleton keeper class
     */
    private static class ExpParserSingleton {
        private static final ExpParser singleton = new ExpParser();
    }
}
