package homework.poly.parse.matcher;

import homework.poly.PolyExp;

import java.util.LinkedList;

public class PolyMultiMatcher {
    private LinkedList<PolyPattern> patterns = new LinkedList<>();
    private PolyPattern cachedPattern;

    private PolyMultiMatcher() {
        addMatcher("c*x^i");
        addMatcher("+x^i");
        addMatcher("x^i");
        addMatcher("c*x");
        addMatcher("+x");
        addMatcher("x");
        addMatcher("c", true);
    }

    public static PolyMultiMatcher getInstance() {
        return PolyMatcherMgrSingleton.singleton;
    }

    private void addMatcher(String polyPattern, boolean isConstant) {
        PolyPattern matcher = PolyPattern.compile(polyPattern, isConstant);
        patterns.add(matcher);
    }

    private void addMatcher(String polyPattern) {
        addMatcher(polyPattern, false);
    }

    public void matchAll(String str, boolean debug) {
        for (PolyPattern pattern :
            patterns) {
            pattern.matcher(str);
        }
        if (debug) {
            System.out.println("===========================");
            System.out.println("Matching " + str);
        }
    }

    public boolean findAny(boolean debug) {
        boolean res = false;
        PolyPattern currentBestPtn = null;
        int maxLen = 0;
        for (PolyPattern matcher :
            patterns) {
            boolean find = matcher.find();
            if (debug) {
                System.out.println("matcher = " + matcher);
                System.out.println("find = " + find);
            }
            if (find) {
                res = true;
                if (matcher.getMatchedString().length() > maxLen) {
                    currentBestPtn = matcher;
                    maxLen = matcher.getMatchedString().length();
                }
            }
        }
        cachedPattern = currentBestPtn;
        if (debug) {
            System.out.println("cachedPattern = " + cachedPattern);
        }
        return res;
    }

    public PolyExp getTermExp() {
        return cachedPattern.getTermExp();
    }

    public int getMatchedLength() {
        return cachedPattern.getMatchedString().length();
    }

    private static class PolyMatcherMgrSingleton {
        private static final PolyMultiMatcher singleton =
            new PolyMultiMatcher();
    }
}
