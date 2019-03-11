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

    /**
     * return a set whose index = oriInd - (0, 2, 0) (or (0, 0, 2))
     *
     * @param isCos if true, (a, b, c-2) as removed cos^2, otherwise (a, b-2, c)
     * @return the hash set
     */
    private HashSet<TriIndex> getTrigoIndSet(boolean isCos,
                                             boolean onlyAddThoseMustMerged) {
        HashSet<TriIndex> set = new HashSet<>(termCoefMap.size());
        for (TriIndex triIndex :
            termCoefMap.keySet()) {
            TriIndex rootIndex;
            if (isCos) {
                rootIndex = triIndex.add(0, 0, -2);
            } else {
                rootIndex = triIndex.add(0, -2, 0);
            }
            if (onlyAddThoseMustMerged && mustMerge(rootIndex, BigInteger.ONE
                , BigInteger.TEN)) {
                set.add(rootIndex);
            }
        }
        return set;
    }

    private boolean mustMerge(TriIndex rootTriIndex, BigInteger sinCoef,
                              BigInteger cosCoef) {
        // the term seems like coef * x^a * sin^b * cos^c
        // where b!=-1, b!=-2, c!=-1, c!=-2
        // can must be optimized
        return
            sinCoef.equals(cosCoef) || (
                !rootTriIndex.getCosInd().equals(BigInteger.valueOf(-1))
                    && !rootTriIndex.getSinInd().equals(BigInteger.valueOf(-1))
                    && !rootTriIndex.getCosInd().equals(BigInteger.valueOf(-2))
                    && !rootTriIndex.getSinInd().equals(BigInteger.valueOf(-2))
            );
    }

    private boolean mergeTrigoOnce(boolean onlyMergeThoseMustMerged,
                                   boolean isCos) {
        boolean modified = false;
        for (TriIndex curIndex : new HashSet<>(termCoefMap.keySet())) {
            TriIndex curCos2Index;
            TriIndex curRootIndex;
            TriIndex curSin2Index;
            if (isCos) {
                curCos2Index = curIndex;
                curRootIndex = curCos2Index.add(0, 0, -2);
                curSin2Index = curRootIndex.add(0, 2, 0);
            } else {
                curSin2Index = curIndex;
                curRootIndex = curSin2Index.add(0, -2, 0);
                curCos2Index = curRootIndex.add(0, 0, 2);
            }
            BigInteger sin2Coef = termCoefMap.get(curSin2Index);
            BigInteger cos2Coef = termCoefMap.get(curCos2Index);
            BigInteger root2Coef = termCoefMap.get(curRootIndex);
            if (sin2Coef == null && cos2Coef == null) {
                continue;
            }
            BigInteger resSin2Coef = sin2Coef;
            BigInteger resCos2Coef = cos2Coef;
            if (root2Coef != null) {
                resSin2Coef = root2Coef;
                resCos2Coef = root2Coef;
                if (sin2Coef != null) {
                    resSin2Coef = sin2Coef.add(root2Coef);
                }
                if (cos2Coef != null) {
                    resCos2Coef = cos2Coef.add(root2Coef);
                }
            }
            if (resCos2Coef == null || resSin2Coef == null) {
                continue;
            }
            if (onlyMergeThoseMustMerged &&
                !mustMerge(curRootIndex, resSin2Coef, resCos2Coef)) {
                continue;
            }
            BigInteger resRootCoef;

            // check abs & merge
            if (resSin2Coef.abs().compareTo(resCos2Coef.abs()) > 0) {
                // remove cos
                resSin2Coef = resSin2Coef.subtract(resCos2Coef);
                resRootCoef = resCos2Coef;
                resCos2Coef = null;
            } else {
                // remove sin
                resCos2Coef = resCos2Coef.subtract(resSin2Coef);
                resRootCoef = resSin2Coef;
                resSin2Coef = null;
            }
            if ((resSin2Coef != null) && resSin2Coef.equals(sin2Coef) ||
                (resCos2Coef != null) && resCos2Coef.equals(cos2Coef)) {
                continue;
            }

            updateMap(curRootIndex, curSin2Index, curCos2Index, resRootCoef,
                resSin2Coef, resCos2Coef);

            // update modified
            modified = true;
        }
        return modified;
    }

    private void updateMap(TriIndex curRootIndex, TriIndex curSin2Index,
                           TriIndex curCos2Index, BigInteger resRootCoef,
                           BigInteger resSin2Coef, BigInteger resCos2Coef) {
        if (resCos2Coef == null) {
            termCoefMap.remove(curCos2Index);
        } else {
            termCoefMap.put(curCos2Index, resCos2Coef);
        }
        if (resSin2Coef == null) {
            termCoefMap.remove(curSin2Index);
        } else {
            termCoefMap.put(curSin2Index, resSin2Coef);
        }
        termCoefMap.put(curRootIndex, resRootCoef);
    }

    private void mergeTrigo() {
        while (true) {
            if (!mergeTrigoOnce(true, true)
                && !mergeTrigoOnce(true, false)) {
                break;
            }
        }
    }

    /**
     * terms like 1-sin^2 = cos^2
     */
    private boolean mergeTrigoAndRootOnce(boolean isCos) {
        HashSet<TriIndex> trigoSet = getTrigoIndSet(isCos, false);
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


    /**
     * get a string from giving map copy
     *
     * @param termCoefMap a hash map
     * @return a string
     */
    private String getExpStringOf(HashMap<TriIndex, BigInteger> termCoefMap) {
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

    @Override
    public String toString() {
        mergeTrigo();
        System.out.println("getExpStringOf(termCoefMap) = "
            + getExpStringOf(termCoefMap));
        return getExpStringOf(termCoefMap);
    }
}
