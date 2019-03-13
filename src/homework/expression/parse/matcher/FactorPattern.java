package homework.expression.parse.matcher;

import homework.expression.core.DefaultExpFactory;
import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactorPattern {
    private static final String BIG_INT_PATTERN = "([-+]?[0-9]+)";
    private static final String PM_PATTERN = "([-+])";
    private static final String BLANK_PATTERN = "[ \\t]*";
    private static final String SIN_PATTERN = "sin" + BLANK_PATTERN +
        "\\(" + BLANK_PATTERN + "x" + BLANK_PATTERN + "\\)";
    private static final String COS_PATTERN = "cos" + BLANK_PATTERN +
        "\\(" + BLANK_PATTERN + "x" + BLANK_PATTERN + "\\)";
    private static final ExpFactory FACTORY = DefaultExpFactory.getInstance();
    private String rePatternStr;
    private String factorPatternStr;
    private Pattern rePattern;
    private Matcher matcher;
    private int coefGroupInd = 0;
    private int indexGroupInd = 0;
    private BigInteger defaultIndex = BigInteger.ONE;
    private BigInteger defaultCoef = BigInteger.ONE;
    private Expression coreFactorExp = FACTORY.var();

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
            throw new Error("Bad expression pattern str: " +
                polyPatternStr);
        }
        return new FactorPattern(polyPatternStr, setDefaultIndexToZero);
    }

    Expression getFactorExp() {
        return coreFactorExp.pow(coreFactorExp.constant(getIndex()))
            .mul(coreFactorExp.constant(getCoef()));
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
                    coreFactorExp = FACTORY.constant(1);
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
                    coreFactorExp = FACTORY.var();
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
                    coreFactorExp = FACTORY.var().sin();
                    break;
                }
                case 'C': {
                    builder.append(COS_PATTERN);
                    coreFactorExp = FACTORY.var().cos();
                    break;
                }
                default: {
                    throw new Error("Unexpected char \'" + c + "\'");
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
