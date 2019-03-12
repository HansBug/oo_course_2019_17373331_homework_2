package homework.tri;

import homework.util.Triplet;
import javafx.util.Pair;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

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

    private static String getTermStringOf(BigInteger coef, TriIndex triIndex) {
        if (coef.equals(BigInteger.ZERO)) {
            return "";
        }
        String factor = triIndex.toString();
        String coefStr = coef.toString();
        if (coef.compareTo(BigInteger.ZERO) > 0) {
            coefStr = "+" + coefStr;
        }
        if (!factor.isEmpty()) {
            if (coef.equals(BigInteger.ONE)) {
                return "+" + factor;
            } else if (coef.equals(BigInteger.ONE.negate())) {
                return "-" + factor;
            } else {
                return coefStr + "*" + factor;
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
            if (onlyAddThoseMustMerged && isGoodToMerge(rootIndex,
                BigInteger.ONE
                , BigInteger.TEN)) {
                set.add(rootIndex);
            }
        }
        return set;
    }

    private boolean isGoodToMerge(TriIndex rootTriIndex, BigInteger sinCoef,
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
                                   boolean isCos,
                                   int level) {
        return mergeTrigoOnce(onlyMergeThoseMustMerged, isCos,
            termCoefMap, level);
    }

    private boolean mergeTrigoOnce(boolean onlyMergeThoseMustMerged,
                                   boolean isCos,
                                   HashMap<TriIndex, BigInteger> termCoefMap,
                                   int level) {
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
            BigInteger rootCoef = termCoefMap.get(curRootIndex);
            if ((sin2Coef == null && cos2Coef == null) ||
                (rootCoef == null && (sin2Coef == null || cos2Coef == null))) {
                // if two of them are null, continue
                continue;
            }
            BigInteger resSin2Coef = sin2Coef;
            BigInteger resCos2Coef = cos2Coef;
            if (rootCoef != null) {
                resSin2Coef = rootCoef;
                resCos2Coef = rootCoef;
                if (sin2Coef != null) {
                    resSin2Coef = sin2Coef.add(rootCoef);
                }
                if (cos2Coef != null) {
                    resCos2Coef = cos2Coef.add(rootCoef);
                }
            }

            if (onlyMergeThoseMustMerged &&
                !isGoodToMerge(curRootIndex, resSin2Coef, resCos2Coef)) {
                continue;
            }

            BigInteger resRootCoef;

            // check abs & merge
            Triplet<BigInteger, BigInteger, BigInteger> mergedTriCoef =
                getOptimizedCoefs(resSin2Coef, resCos2Coef,
                    curRootIndex, curSin2Index, curCos2Index, level);
            resRootCoef = mergedTriCoef.getFirst();
            resSin2Coef = mergedTriCoef.getSecond();
            resCos2Coef = mergedTriCoef.getThird();


            if ((resCos2Coef == null || resCos2Coef.equals(cos2Coef)) &&
                (resSin2Coef == null || resSin2Coef.equals(sin2Coef))) {
                // if resRoot==root && resSin==sin
                // than must resCos==cos
                continue;
            }

            updateMap(curRootIndex, curSin2Index, curCos2Index, resRootCoef,
                resSin2Coef, resCos2Coef, termCoefMap);

            // update modified
            modified = true;
        }
        return modified;
    }

    /**
     * try to find out the best coef of given sin^2 and cos^2
     *
     * @param sin2Coef  coef of sin^2
     * @param cos2Coef  coef of cos^2
     * @param rootIndex index of root
     * @param sinIndex  index of sin^2
     * @param cosIndex  index of cos^2
     * @param level     if 0 : weak mode. merge depend on abs(coef)
     *                  if 10: must merge, merge depend on the length
     *                  if 100: not must merge, merge depend on the length
     * @return though its a TriIndex, it contains coefs.
     */
    private Triplet<BigInteger, BigInteger, BigInteger> getOptimizedCoefs(
        BigInteger sin2Coef, BigInteger cos2Coef,
        TriIndex rootIndex, TriIndex sinIndex, TriIndex cosIndex,
        int level) {

        BigInteger resRootCoef;
        BigInteger resSin2Coef;
        BigInteger resCos2Coef = null;

        String bestStr;

        if (level == 0) {
            if (sin2Coef.abs().compareTo(cos2Coef.abs()) > 0) {
                //remove cos
                return new TriIndex(cos2Coef, //root
                    sin2Coef.subtract(cos2Coef), //sin
                    null); //cos
            } else {
                //remove sin
                return new TriIndex(sin2Coef, // root
                    null, // sin
                    cos2Coef.subtract(sin2Coef));//cos
            }
        } else if (level > 0) {
            // remove cos
            resSin2Coef = sin2Coef.subtract(cos2Coef);
            resRootCoef = cos2Coef;
            bestStr = getTermStringOf(resSin2Coef, sinIndex) +
                getTermStringOf(resRootCoef, rootIndex);

            BigInteger tmpRootCoef;
            BigInteger tmpSin2Coef;
            BigInteger tmpCos2Coef;

            String currentStr;

            // remove sin
            tmpCos2Coef = cos2Coef.subtract(sin2Coef);
            tmpRootCoef = sin2Coef;
            currentStr = getTermStringOf(tmpCos2Coef, cosIndex) +
                getTermStringOf(tmpRootCoef, rootIndex);
            if (currentStr.length() < bestStr.length()) {
                bestStr = currentStr;
                resCos2Coef = tmpCos2Coef;
                resRootCoef = tmpRootCoef;
                resSin2Coef = null;
            }

            if (level >= 100) {
                currentStr = getTermStringOf(sin2Coef, sinIndex) +
                    getTermStringOf(cos2Coef, cosIndex);
                if (currentStr.length() < bestStr.length()) {
                    return new TriIndex(null, sin2Coef, cos2Coef);
                }
            }

            return new Triplet<>(resRootCoef, resSin2Coef, resCos2Coef);

        } else {
            throw new ValueException("wrong level:" + level);
        }
    }

    private void updateMap(TriIndex curRootIndex, TriIndex curSin2Index,
                           TriIndex curCos2Index, BigInteger resRootCoef,
                           BigInteger resSin2Coef, BigInteger resCos2Coef,
                           HashMap<TriIndex, BigInteger> termCoefMap) {
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
            if (!mergeTrigoOnce(true, true, 0)
                && !mergeTrigoOnce(true, false, 0)) {
                break;
            }
        }

        // greater level
        while (true) {
            if (!mergeTrigoOnce(true, true, 10)
                && !mergeTrigoOnce(true, false, 10)) {
                break;
            }
        }

        while (true) {
            if (!mergeTrigoOnce(true, true, 100)
                && !mergeTrigoOnce(true, false, 100)) {
                break;
            }
        }

        HashMap<TriIndex, BigInteger> copy = new HashMap<>(termCoefMap);
        while (true) {
            if (!mergeTrigoOnce(false, true,
                copy, 0)
                && !mergeTrigoOnce(false, false,
                copy, 0)) {
                break;
            }
        }
        if (getExpStringOf(copy).length() <
            getExpStringOf(termCoefMap).length()) {
            termCoefMap = copy;
        }
        while (true) {
            if (!mergeTrigoOnce(false, true,
                copy, 10)
                && !mergeTrigoOnce(false, false,
                copy, 10)) {
                break;
            }
        }
        if (getExpStringOf(copy).length() <
            getExpStringOf(termCoefMap).length()) {
            termCoefMap = copy;
        }

        while (true) {
            if (!mergeTrigoOnce(false, true,
                copy, 100)
                && !mergeTrigoOnce(false, false,
                copy, 100)) {
                break;
            }
        }
        if (getExpStringOf(copy).length() <
            getExpStringOf(termCoefMap).length()) {
            termCoefMap = copy;
        }
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
        termCoefMap.forEach((k, v) -> {
            if (v != null) {
                list.add(new Pair<>(k, v));
            }
        });
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
        return getExpStringOf(termCoefMap);
    }
}
