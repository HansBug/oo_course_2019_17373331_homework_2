package homework.expression.parse.matcher;

import homework.expression.core.ExpFactory;
import homework.expression.core.Expression;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactorPattern {
    private static String BIG_INT_PATTERN = "([-+]?[0-9]+)";
    private static String PM_PATTERN = "([-+])";
    private static String SIN_PATTERN = "sin\\(x\\)";
    private static String COS_PATTERN = "cos\\(x\\)";
    private static String BLANK_PATTERN = "[ \\t]*";
    private String rePatternStr;
    private String factorPatternStr;
    private Pattern rePattern;
    private Matcher matcher;
    private int coefGroupInd = 0;
    private int indexGroupInd = 0;
    private BigInteger defaultIndex = BigInteger.ONE;
    private BigInteger defaultCoef = BigInteger.ONE;

    private Expression coreFactorExp = ExpFactory.var();

    private FactorPattern(String factorPatternStr,
                          boolean setDefaultIndexToZero) {
        this.factorPatternStr = factorPatternStr;
        rePatternStr = buildRePatternStr();
        rePattern = Pattern.compile(rePatternStr);
        if (setDefaultIndexToZero) {
            defaultIndex = BigInteger.ZERO;
        }
    }

    /**
     * compile a matcher by a factorPatternStr String.
     * <p>
     * reg exp can be compiled automatically
     *
     * @param polyPatternStr        a factorPatternStr string.
     *                              a factorPatternStr should be a string
     *                              contains
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
    public static FactorPattern compile(String polyPatternStr,
                                        boolean setDefaultIndexToZero) {
        String striped = polyPatternStr.replaceAll("\\s", "");
        Pattern checkPattern = Pattern.compile("^[c+*x^iSC]+$");
        if (!checkPattern.matcher(striped).find()) {
            throw new ValueException("Bad expression pattern str: " +
                polyPatternStr);
        }
        return new FactorPattern(polyPatternStr, setDefaultIndexToZero);
    }

    Expression getFactorExp() {
        return ExpFactory.pow(
            ExpFactory.mul(getCoef(), coreFactorExp),
            getIndex());
    }

    public String getFactorPatternStr() {
        return factorPatternStr;
    }

    private String buildRePatternStr() {
        StringBuilder builder = new StringBuilder();
        builder.append("^");
        builder.append(BLANK_PATTERN);
        int groupCount = 1;
        for (char c :
            getFactorPatternStr().toCharArray()) {
            switch (c) {
                case 'c': {
                    builder.append(BIG_INT_PATTERN);
                    coefGroupInd = groupCount;
                    groupCount += 1;
                    coreFactorExp = ExpFactory.constant(1);
                    break;
                }
                case 'i': {
                    builder.append(BIG_INT_PATTERN);
                    indexGroupInd = groupCount;
                    groupCount += 1;
                    break;
                }
                case '+': {
                    builder.append(PM_PATTERN);
                    coefGroupInd = groupCount;
                    groupCount += 1;
                    break;
                }
                case 'x': {
                    builder.append("x");
                    coreFactorExp = ExpFactory.var();
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
                case 'S': {
                    builder.append(SIN_PATTERN);
                    coreFactorExp = ExpFactory.sin(ExpFactory.var());
                    break;
                }
                case 'C': {
                    builder.append(COS_PATTERN);
                    coreFactorExp = ExpFactory.cos(ExpFactory.var());
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

    String getMatchedString() {
        return matcher.group(0);
    }

    @Override
    public String toString() {
        return "FactorPattern{" +
            "\'" + factorPatternStr + '\'' +
            '}';
    }
}
