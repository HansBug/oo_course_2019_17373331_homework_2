package homework.expression;

import javafx.util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TriExp {
    private HashMap<TriIndex, BigInteger> termCoefMap;

    private TriExp(HashMap<TriIndex, BigInteger> termCoefMap) {
        this.termCoefMap = termCoefMap;
    }

    private TriExp(int initialCapacity) {
        this(new HashMap<>(initialCapacity));
    }

    TriExp() {
        this(16);
    }

    public TriExp(BigInteger initialCoef, TriIndex initialTriIndex) {
        this();
        termCoefMap.put(initialTriIndex, initialCoef);
    }

    private static String toFactorString(BigInteger index, String factorStr) {
        if (index.equals(BigInteger.ZERO)) {
            return "";
        } else if (index.equals(BigInteger.ONE)) {
            return factorStr;
        } else {
            return factorStr + "^" + index.toString();
        }
    }

    private static String toTermString(BigInteger coef, TriIndex triIndex) {
        if (coef.equals(BigInteger.ZERO)) {
            return "";
        }
        BigInteger varIndex = triIndex.getInd1();
        BigInteger sinIndex = triIndex.getInd2();
        BigInteger cosIndex = triIndex.getInd3();
        StringBuilder builder = new StringBuilder();
        String var = toFactorString(varIndex, "x");
        String sin = toFactorString(sinIndex, "sin(x)");
        String cos = toFactorString(cosIndex, "cos(x)");
        if (!var.equals("")) {
            builder.append("*");
            builder.append(var);
        }
        if (!sin.equals("")) {
            builder.append("*");
            builder.append(sin);
        }
        if (!cos.equals("")) {
            builder.append("*");
            builder.append(cos);
        }
        String factor = builder.toString();
        String coefStr = coef.toString();
        if (coef.compareTo(BigInteger.ZERO) > 0) {
            coefStr = "+" + coefStr;
        }
        if (!"".equals(factor)) {
            if (coef.equals(BigInteger.ONE)) {
                return "+" + factor.substring(1);
            } else if (coef.equals(BigInteger.ONE.negate())) {
                return "-" + factor.substring(1);
            } else {
                return coefStr + factor;
            }
        } else {
            return coefStr;
        }
    }

    void append(TriIndex triIndex, BigInteger coef) {
        if (termCoefMap.containsKey(triIndex)) {
            termCoefMap.replace(triIndex, termCoefMap.get(triIndex).add(coef));
        } else {
            termCoefMap.put(triIndex, coef);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        List<Pair<TriIndex, BigInteger>> list =
            new ArrayList<>(termCoefMap.size());
        termCoefMap.forEach((k, v) -> list.add(new Pair<>(k, v)));
        list.sort(Comparator.comparing(p -> p.getValue().negate()));
        list.forEach(p -> builder.append(toTermString(p.getValue(),
            p.getKey())));
        String res = builder.toString();
        if ("".equals(res)) {
            return "0";
        }
        if (res.charAt(0) == '+') {
            res = res.substring(1);
        }
        return res;
    }
}
