package homework.tri;

import javafx.util.Pair;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    private static String getFactorStringOf(BigInteger index,
                                            String factorStr) {
        if (index.equals(BigInteger.ZERO)) {
            return "";
        } else if (index.equals(BigInteger.ONE)) {
            return factorStr;
        } else {
            return factorStr + "^" + index.toString();
        }
    }

    private static String getTermStringOf(BigInteger coef, TriIndex triIndex) {
        if (coef.equals(BigInteger.ZERO)) {
            return "";
        }
        BigInteger varIndex = triIndex.getVarInd();
        BigInteger sinIndex = triIndex.getSinInd();
        BigInteger cosIndex = triIndex.getCosInd();
        StringBuilder builder = new StringBuilder();
        String var = getFactorStringOf(varIndex, "x");
        String sin = getFactorStringOf(sinIndex, "sin(x)");
        String cos = getFactorStringOf(cosIndex, "cos(x)");
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

    void addAppend(TriIndex triIndex, BigInteger coef) {
        if (termCoefMap.containsKey(triIndex)) {
            BigInteger newValue = termCoefMap.get(triIndex).add(coef);
            if (newValue.equals(BigInteger.ZERO)) {
                termCoefMap.remove(triIndex);
            } else {
                termCoefMap.replace(triIndex, newValue);
            }
        } else {
            termCoefMap.put(triIndex, coef);
        }
    }

    HashSet<TriIndex> getTrigoIndSet(boolean isCos) {
        HashSet<TriIndex> set = new HashSet<>(termCoefMap.size());
        BigInteger bigTwo = BigInteger.valueOf(2);
        for (TriIndex triIndex :
            termCoefMap.keySet()) {
            BigInteger targetInd;
            if (isCos) {
                targetInd = triIndex.getCosInd();
            } else {
                targetInd = triIndex.getSinInd();
            }
            if (targetInd.compareTo(BigInteger.ONE) > 0) {
                TriIndex resIndex;
                if (isCos) {
                    resIndex = triIndex.add(0, 0, -2);
                } else {
                    resIndex = triIndex.add(0, -2, 0);
                }
                set.add(resIndex);
            }
        }
        return set;
    }

    private boolean mergeTrigoOnce() {
        HashSet<TriIndex> cosSet = getTrigoIndSet(true);
        HashSet<TriIndex> sinSet = getTrigoIndSet(false);
        boolean modified = false;
        for (TriIndex targetIndex : cosSet) {
            if (sinSet.contains(targetIndex)) {
                TriIndex sinOriIndex = targetIndex.add(0, 2, 0);
                TriIndex cosOriIndex = targetIndex.add(0, 0, 2);
                BigInteger sinCoef = termCoefMap.get(sinOriIndex);
                BigInteger cosCoef = termCoefMap.get(cosOriIndex);
                if (sinCoef.abs().compareTo(cosCoef.abs()) > 0) {
                    // remove cos
                    addAppend(sinOriIndex, cosCoef.negate());
                    termCoefMap.remove(cosOriIndex);
                    addAppend(targetIndex, cosCoef);
                } else {
                    // remove sin
                    addAppend(cosOriIndex, sinCoef.negate());
                    termCoefMap.remove(sinOriIndex);
                    addAppend(targetIndex, sinCoef);
                }
                modified = true;
            }
        }
        return modified;
    }

    void mergeTrigo() {
        while (true) {
            if (!mergeTrigoOnce()
                && !mergeTrigoAndRootOnce(true)
                && !mergeTrigoAndRootOnce(false)) {
                break;
            }
        }
    }

    /**
     * terms like 1-sin^2 = cos^2
     */
    boolean mergeTrigoAndRootOnce(boolean isCos) {
        HashSet<TriIndex> trigoSet = getTrigoIndSet(isCos);
        boolean modified = false;
        for (TriIndex targetIndex : trigoSet) {
            TriIndex trigoOriIndex;
            if (isCos) {
                trigoOriIndex = targetIndex.add(0, 0, 2);
            } else {
                trigoOriIndex = targetIndex.add(0, 2, 0);
            }
            BigInteger trigoCoef = termCoefMap.get(trigoOriIndex);
            BigInteger rootCoef = termCoefMap.get(targetIndex);
            if (trigoCoef.negate().equals(rootCoef)) {
                termCoefMap.remove(targetIndex);
                termCoefMap.remove(trigoOriIndex);
                if (isCos) {
                    addAppend(targetIndex.add(0, 2, 0), rootCoef);
                } else {
                    addAppend(targetIndex.add(0, 0, 2), rootCoef);
                }
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public String toString() {
        mergeTrigo();
        StringBuilder builder = new StringBuilder();
        List<Pair<TriIndex, BigInteger>> list =
            new ArrayList<>(termCoefMap.size());
        termCoefMap.forEach((k, v) -> list.add(new Pair<>(k, v)));
        list.sort((v1, v2) -> {
            if (v1.getValue().equals(v2.getValue())) {
                return v1.getKey().compareTo(v2.getKey());
            }
            return v1.getValue().negate().compareTo(v2.getValue().negate());
        });
        list.forEach(p -> builder.append(getTermStringOf(p.getValue(),
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
