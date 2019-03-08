package homework.poly;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Poly Expression.
 * <p>
 * contains a sort of poly terms
 */
public class PolyExp implements Derivable {

    private HashMap<BigInteger, PolyTerm> terms;

    public PolyExp() {
        this(16);
    }

    public PolyExp(int initialCapacity) {
        terms = new HashMap<>(initialCapacity);
    }


    /**
     * Shouldn't use this one to create a new PolyExp
     * <p>
     * you should use PolyExp(int, int) or
     * PolyExp(BigInteger, BigInteger) instead
     *
     * @param term initial PolyTerm
     */
    PolyExp(PolyTerm term) {
        this();
        terms.put(term.getIndex(), term);
    }

    public PolyExp(BigInteger index, BigInteger coef) {
        this(new PolyTerm(index, coef));
    }

    public PolyExp(int index, int coef) {
        this(new PolyTerm(index, coef));
    }

    public PolyExp add(PolyExp other) {
        other.getTerms().forEach((k, v) -> terms.merge(k, v, PolyTerm::add));
        return this;
    }

    PolyExp add(PolyTerm term) {
        BigInteger index = term.getIndex();
        if (terms.containsKey(index)) {
            PolyTerm termExisted = terms.get(index);
            PolyTerm termAdded = termExisted.add(term);
            terms.replace(index, termAdded);
        } else {
            terms.put(index, term);
        }
        return this;
    }

    private HashMap<BigInteger, PolyTerm> getTerms() {
        return terms;
    }

    @Override
    public PolyExp getDerivative() {
        PolyExp derExp = new PolyExp();
        terms.forEach((k, v) -> derExp.add(v.getDerivative()));
        return derExp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolyExp exp = (PolyExp) o;
        return toSortedString().equals(exp.toSortedString());
    }


    /**
     * to sorted string
     *
     * @return sorted string
     */
    @Override
    public String toString() {
        return toSortedString();
    }

    private String toSortedString() {
        StringBuilder builder = new StringBuilder();
        ArrayList<PolyTerm> list = new ArrayList<>(terms.values());
        list.sort(PolyTerm::compareTo);
        list.forEach(builder::append);
        String res = builder.toString();
        if (res.isEmpty()) {
            res = "0";
        }
        if (res.charAt(0) == '+') {
            res = res.substring(1);
        }
        return res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }
}
