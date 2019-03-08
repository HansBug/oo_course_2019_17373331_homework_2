package homework.poly.parse.matcher;

import homework.poly.PolyExp;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PolyPattern {
    private static String BIG_INT_PATTERN = "([-+]?[0-9]+)";
    private static String PM_PATTERN = "([-+])";
    private static String COEF_PM_PATTERN = "([-+])?";
    private static String BLANK_PATTERN = "[\\ \\t]*";
    private final int signGroupInd = 1;
    private String rePatternStr;
    private String polyPatternStr;
    private Pattern rePattern;
    private Matcher matcher;
    private int coefGroupInd = 0;
    private int indexGroupInd = 0;
    private BigInteger defaultIndex = BigInteger.ONE;
    private BigInteger defaultCoef = BigInteger.ONE;

    private PolyPattern(String polyPatternStr, boolean setDefaultIndexToZero) {
        this.polyPatternStr = polyPatternStr;
        rePatternStr = buildRePatternStr();
        rePattern = Pattern.compile(rePatternStr);
        if (setDefaultIndexToZero) {
            defaultIndex = BigInteger.ZERO;
        }
    }

    /**
     * compile a matcher by a polyPatternStr String.
     * <p>
     * reg exp can be compiled automatically
     *
     * @param polyPatternStr        a polyPatternStr string.
     *                              a polyPatternStr should be a string contains
     *                              only [ c | + | * | x | ^ | i ]
     *                              <p>
     *                              where
     *                              <p>
     *                              - c is a number placeholder, for coef
     *                              <p>
     *                              - + is a sign placeholder, for +/- before x
     *                              (e.g. -x, + x, - x^2)
     *                              <p>
     *                              - i is a number placeholder, for index
     * @param setDefaultIndexToZero set the default index value from 1 to 0
     */
    public static PolyPattern compile(String polyPatternStr,
                                      boolean setDefaultIndexToZero) {
        String striped = polyPatternStr.replaceAll("\\s", "");
        Pattern checkPattern = Pattern.compile("^[c+*x^i]+$");
        if (!checkPattern.matcher(striped).find()) {
            throw new ValueException("Bad poly pattern str: " + polyPatternStr);
        }
        return new PolyPattern(polyPatternStr, setDefaultIndexToZero);
    }

    public String getPolyPatternStr() {
        return polyPatternStr;
    }

    private String buildRePatternStr() {
        StringBuilder builder = new StringBuilder();
        builder.append("^");
        builder.append(BLANK_PATTERN);
        builder.append(PM_PATTERN);
        builder.append(BLANK_PATTERN);
        int groupCount = 2;
        for (char c :
            getPolyPatternStr().toCharArray()) {
            switch (c) {
                case 'c': {
                    builder.append(BIG_INT_PATTERN);
                    coefGroupInd = groupCount;
                    groupCount += 1;
                    break;
                }
                case 'i': {
                    builder.append(BIG_INT_PATTERN);
                    indexGroupInd = groupCount;
                    groupCount += 1;
                    break;
                }
                case '+': {
                    builder.append(COEF_PM_PATTERN);
                    coefGroupInd = groupCount;
                    groupCount += 1;
                    break;
                }
                case 'x': {
                    builder.append("x");
                    break;
                }
                case '^': {
                    builder.append("\\^");
                    break;
                }
                case '*': {
                    builder.append("\\*");
                    break;
                }
                default: {
                    throw new ValueException("Unexpected char \'" + c + "\'");
                }
            }
            builder.append(BLANK_PATTERN);
        }
        return builder.toString();
    }

    Matcher matcher(String str) {
        matcher = rePattern.matcher(str);
        return matcher;
    }

    boolean find() {
        return matcher.find();
    }

    private BigInteger getSign() {
        if (matcher.group(signGroupInd) == null) {
            return BigInteger.ONE;
        }
        if (matcher.group(signGroupInd).equals("-")) {
            return BigInteger.ONE.negate();
        } else {
            return BigInteger.ONE;
        }
    }

    private BigInteger getCoef() {
        if (coefGroupInd == 0) {
            return defaultCoef;
        }
        String coefStr = matcher.group(coefGroupInd);
        if (coefStr == null) {
            return BigInteger.ONE;
        } else if (coefStr.equals("-")) {
            return BigInteger.ONE.negate();
        } else if (coefStr.equals("+")) {
            return BigInteger.ONE;
        }
        return new BigInteger(coefStr);
    }

    private BigInteger getIndex() {
        if (indexGroupInd == 0) {
            return defaultIndex;
        }
        return new BigInteger(matcher.group(indexGroupInd));
    }

    PolyExp getTermExp() {
        return new PolyExp(getIndex(), getCoef().multiply(getSign()));
    }

    String getMatchedString() {
        return matcher.group(0);
    }

    @Override
    public String toString() {
        return "PolyPattern{" +
            "\'" + polyPatternStr + '\'' +
            '}';
    }
}
