package homework.tri;

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
