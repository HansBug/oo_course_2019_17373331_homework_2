package homework.expression.core;

import java.math.BigInteger;
import java.util.Objects;

public class ComputableTester implements Computable {

    private BigInteger value;

    ComputableTester(BigInteger value) {
        this.value = value;
    }

    public ComputableTester(long value) {
        this(BigInteger.valueOf(value));
    }

    @Override
    public Computable add(Computable other) {
        assert other instanceof ComputableTester;
        return new ComputableTester(
            value.add(((ComputableTester) other).value));
    }

    @Override
    public Computable mul(Computable other) {
        assert other instanceof ComputableTester;
        return new ComputableTester(
            value.multiply(((ComputableTester) other).value));
    }

    @Override
    public Computable pow(Computable index) {
        assert index instanceof ComputableTester;
        BigInteger indexValue = ((ComputableTester) index).value;
        return new ComputableTester(value.modPow(indexValue,
            BigInteger.valueOf(1000007)));
    }

    @Override
    public Computable constant(BigInteger bigInteger) {
        return new ComputableTester(bigInteger);
    }

    @Override
    public Computable sin() {
        return new ComputableTester(value.add(BigInteger.valueOf(7)));
    }

    @Override
    public Computable cos() {
        return new ComputableTester(value.negate());
    }

    @Override
    public Computable variable() {
        return constant(this.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ComputableTester that = (ComputableTester) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
