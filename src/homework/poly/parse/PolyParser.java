package homework.poly.parse;

import homework.poly.PolyExp;
import homework.poly.parse.matcher.PolyMultiMatcher;

import java.util.regex.Pattern;

public class PolyParser {

    /**
     * Default constructor
     */
    private PolyParser() {
    }

    public static PolyParser getInstance() {
        return PolyParserSingleton.singleton;
    }

    /**
     * Parse a PolyExp from giving string
     *
     * @param input just a string
     * @return parsed expression. if failed, return null
     */
    public PolyExp parseExp(String input) {
        return parseExp(input, false);
    }

    public PolyExp parseExp(String input, boolean debug) {
        String buffer = preprocessInput(input);
        PolyMultiMatcher matcher = PolyMultiMatcher.getInstance();
        PolyExp resExp = new PolyExp();
        while (!buffer.isEmpty()) {
            matcher.matchAll(buffer, debug);
            if (matcher.findAny(debug)) {
                resExp.add(matcher.getTermExp());
                buffer = buffer.substring(matcher.getMatchedLength());
            } else {
                return null;
            }
        }
        return resExp;
    }

    /**
     * Check & add a '+' at the head of giving string.
     *
     * @param input just input
     * @return a string whose first visible character is '+' or '-'
     */
    private String preprocessInput(String input) {
        Pattern headSignPattern = Pattern.compile("^\\s*[-+]");
        if (!headSignPattern.matcher(input).find()) {
            return "+" + input;
        } else {
            return input;
        }
    }

    /**
     * Singleton keeper class
     */
    private static class PolyParserSingleton {
        private static final PolyParser singleton = new PolyParser();
    }

}