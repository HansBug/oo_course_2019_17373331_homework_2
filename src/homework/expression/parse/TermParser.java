package homework.expression.parse;

import homework.expression.core.DefaultExpFactory;
import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;
import homework.expression.parse.matcher.FactorMultiMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TermParser {

    private static final ExpFactory FACTORY = DefaultExpFactory.getInstance();
    private String buffer;

    /**
     * Default constructor
     */
    private TermParser() {
    }

    static TermParser getInstance() {
        return PolyParserSingleton.singleton;
    }

    /**
     * Parse a Expression from giving string
     *
     * @param input just a string
     * @return parsed expression. if failed, return null
     */
    public Expression parseTermExp(String input) {
        return parseTermExp(input, false);
    }

    public Expression parseTermExp(String input, boolean debug) {

        // the first +/- can be a 1/-1 factor
        final Pattern pmPtn = Pattern.compile("^[ \\t]*([-+]?)[ \\t]*");
        Matcher pmMatcher = pmPtn.matcher(input);
        Expression resExp;
        if (pmMatcher.find() && pmMatcher.group(1).equals("-")) {
            resExp = FACTORY.constant(-1);
        } else {
            resExp = FACTORY.constant(1);
        }

        // add a '*' to the head
        buffer = preprocessInput(input.substring(pmMatcher.end()));

        FactorMultiMatcher matcher = new FactorMultiMatcher();
        final String starStr = "^[ \\t]*\\*[ \\t]*";
        Pattern starPtn = Pattern.compile(starStr);
        while (!buffer.isEmpty()) {
            Matcher starMarcher = starPtn.matcher(buffer);
            if (starMarcher.find()) {
                buffer = buffer.substring(starMarcher.end());
            } else {
                break;
            }
            matcher.match(buffer, debug);
            if (matcher.find(debug)) {
                if (debug) {
                    System.out.println("matcher.getFactorExp() = "
                        + matcher.getFactorExp());
                }
                resExp = resExp.mul(matcher.getFactorExp());
                buffer = buffer.substring(matcher.getMatchedLength());
            } else {
                return null;
            }
        }
        if (debug) {
            System.out.println("Term Exp = " + resExp);
        }
        return resExp;
    }

    public String getRemainedBuffer() {
        return buffer;
    }

    /**
     * Check & add a '*' at the head of giving string.
     *
     * @param input just input
     * @return a string whose first visible character is '*'
     */
    private String preprocessInput(String input) {
        return "*" + input;
    }

    /**
     * Singleton keeper class
     */
    private static class PolyParserSingleton {
        private static final TermParser singleton = new TermParser();
    }

}