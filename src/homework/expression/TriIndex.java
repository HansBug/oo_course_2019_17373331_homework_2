package homework.expression;

import java.math.BigInteger;
import java.util.Objects;

public class TriIndex {
    static final TriIndex ZERO = new TriIndex(0, 0, 0);
    private BigInteger ind1;
    private BigInteger ind2;
    private BigInteger ind3;

    TriIndex(BigInteger ind1, BigInteger ind2, BigInteger ind3) {
        this.ind1 = ind1;
        this.ind2 = ind2;
        this.ind3 = ind3;
    }

    private TriIndex(long ind1, long ind2, long ind3) {
        this.ind1 = BigInteger.valueOf(ind1);
        this.ind2 = BigInteger.valueOf(ind2);
        this.ind3 = BigInteger.valueOf(ind3);
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

    public TriIndex mul(TriIndex other) {
        return new TriIndex(other.ind1.multiply(ind1),
            other.ind2.multiply(ind2),
            other.ind3.multiply(ind3));
    }

    BigInteger getInd1() {
        return ind1;
    }

    BigInteger getInd2() {
        return ind2;
    }

    BigInteger getInd3() {
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
}
