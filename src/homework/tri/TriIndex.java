package homework.tri;

import homework.util.Triplet;

import java.math.BigInteger;
import java.util.Objects;

public class TriIndex extends Triplet<BigInteger, BigInteger, BigInteger> {
    static final TriIndex ZERO = new TriIndex(0, 0, 0);
    private final BigInteger ind1;
    private final BigInteger ind2;
    private final BigInteger ind3;

    TriIndex(BigInteger ind1, BigInteger ind2, BigInteger ind3) {
        super(ind1, ind2, ind3);
        this.ind1 = getFirst();
        this.ind2 = getSecond();
        this.ind3 = getThird();
    }

    private TriIndex(long ind1, long ind2, long ind3) {
        this(BigInteger.valueOf(ind1),
            BigInteger.valueOf(ind2),
            BigInteger.valueOf(ind3));
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String var = getFactorStringOf(getVarInd(), "x");
        String sin = getFactorStringOf(getSinInd(), "sin(x)");
        String cos = getFactorStringOf(getCosInd(), "cos(x)");
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
        String res = builder.toString();
        if (!res.isEmpty()) {
            res = res.substring(1);
        }
        return res;
    }

    boolean isZeroIndex() {
        return ind1.equals(BigInteger.ZERO) &
            ind2.equals(BigInteger.ZERO) &
            ind3.equals(BigInteger.ZERO);
    }

    public TriIndex add(TriIndex other) {
        return new TriIndex(other.ind1.add(ind1),
            other.ind2.add(ind2),
            other.ind3.add(ind3)
        );
    }

    public TriIndex add(long var, long sin, long cos) {
        return add(new TriIndex(
            BigInteger.valueOf(var),
            BigInteger.valueOf(sin),
            BigInteger.valueOf(cos)
        ));
    }

    public TriIndex mul(TriIndex other) {
        return new TriIndex(other.ind1.multiply(ind1),
            other.ind2.multiply(ind2),
            other.ind3.multiply(ind3));
    }

    BigInteger getVarInd() {
        return ind1;
    }

    BigInteger getSinInd() {
        return ind2;
    }

    BigInteger getCosInd() {
        return ind3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TriIndex triIndex = (TriIndex) o;
        return Objects.equals(ind1, triIndex.ind1) &&
            Objects.equals(ind2, triIndex.ind2) &&
            Objects.equals(ind3, triIndex.ind3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ind1, ind2, ind3);
    }

    int compareTo(TriIndex other) {
        if (!ind1.equals(other.ind1)) {
            return ind1.compareTo(other.ind1);
        } else if (!ind2.equals(other.ind2)) {
            return ind2.compareTo(other.ind2);
        } else {
            return ind3.compareTo(other.ind3);
        }
    }
}
