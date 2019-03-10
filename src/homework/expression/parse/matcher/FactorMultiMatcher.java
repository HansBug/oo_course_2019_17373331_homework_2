package homework.expression.parse.matcher;

import homework.expression.core.Expression;

import java.util.LinkedList;

public class FactorMultiMatcher {
    private LinkedList<FactorPattern> patterns = new LinkedList<>();
    private FactorPattern cachedPattern;

    public FactorMultiMatcher() {
        addMatcher("x");
        addMatcher("S");
        addMatcher("C");
        addMatcher("x^i");
        addMatcher("S^i");
        addMatcher("C^i");
        addMatcher("+x");
        addMatcher("+S");
        addMatcher("+C");
        addMatcher("+x^i");
        addMatcher("+S^i");
        addMatcher("+C^i");
        addMatcher("c");
    }

    private void addMatcher(String polyPattern, boolean isConstant) {
        FactorPattern matcher = FactorPattern.compile(polyPattern, isConstant);
        patterns.add(matcher);
    }

    private void addMatcher(String polyPattern) {
        addMatcher(polyPattern, false);
    }

    public void match(String str, boolean debug) {
        for (FactorPattern pattern :
            patterns) {
            pattern.matcher(str);
        }
    }

    public boolean find(boolean debug) {
        boolean res = false;
        FactorPattern currentBestPtn = null;
        int maxLen = 0;
        for (FactorPattern matcher :
            patterns) {
            boolean find = matcher.find();
            if (find) {
                res = true;
                if (matcher.getMatchedString().length() > maxLen) {
                    currentBestPtn = matcher;
                    maxLen = matcher.getMatchedString().length();
                }
            }
        }
        cachedPattern = currentBestPtn;
        return res;
    }

    public Expression getFactorExp() {
        return cachedPattern.getFactorExp();
    }

    public int getMatchedLength() {
        return cachedPattern.getMatchedString().length();
    }
}
