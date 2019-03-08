package homework.poly;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Poly Term.
 * <p>
 * Contains an index and a coefficient
 */
public class PolyTerm implements Derivable {
    private final BigInteger index;
    private BigInteger coef;

    PolyTerm(BigInteger ind, BigInteger coe) {
        index = ind;
        coef = coe;
    }

    PolyTerm(int ind, int coe) {
        this(new BigInteger(String.valueOf(ind)),
            new BigInteger(String.valueOf(coe)));
    }

    static PolyTerm constant(BigInteger coe) {
        return new PolyTerm(BigInteger.ZERO, coe);
    }

    BigInteger getIndex() {
        return index;
    }

    private BigInteger getCoef() {
        return coef;
    }

    PolyTerm add(PolyTerm other) {
        assert (other.getIndex().equals(index));
        coef = coef.add(other.getCoef());
        return this;
    }

    void negate() {
        coef = coef.negate();
    }

    @Override
    public PolyTerm getDerivative() {
        if (index.equals(BigInteger.ZERO)) {
            return new PolyTerm(BigInteger.ZERO, BigInteger.ZERO);
        } else {
            return new PolyTerm(
                index.subtract(BigInteger.ONE), index.multiply(coef));
        }
    }

    /**
     * format as coef*x^index
     * <p>
     * return "" if coef = 0
     * <p>
     * return x^index if coef = 1
     * <p>
     * return -x^index if coef = -1
     * <p>
     * return coef as a constant if index = 0
     * <p>
     * return coef*x if index = 1
     *
     * @return formatted string
     */
    @Override
    public String toString() {

        // zero
        if (BigInteger.ZERO.equals(coef)) {
            return "";
        }

        String coefStr = coef.toString();
        if (coef.compareTo(BigInteger.ZERO) > 0) {
            coefStr = "+" + coefStr;
        }

        if (BigInteger.ZERO.equals(index)) {
            // constant
            return coefStr;
        } else {
            coefStr = coefStr + "*";
            if (coef.equals(BigInteger.ONE)) {
                coefStr = "+";
            } else if (coef.equals(BigInteger.ONE.negate())) {
                coefStr = "-";
            }
            if (BigInteger.ONE.equals(index)) {
                return coefStr + "x";
            } else {
                return coefStr + "x" + "^" + index.toString();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PolyTerm polyTerm = (PolyTerm) o;
        return Objects.equals(index, polyTerm.index) &&
            Objects.equals(coef, polyTerm.coef);
    }

    /**
     * greater coef first
     * <p>
     * if coef equals, less index first
     *
     * @param other the other one
     * @return -1 if less than, 1 if greater than, 0 if equal
     */
    int compareTo(PolyTerm other) {
        if (coef.compareTo(other.getCoef()) != 0) {
            return -coef.compareTo(other.getCoef());
        } else {
            return index.compareTo(other.getCoef());
        }
    }
}
