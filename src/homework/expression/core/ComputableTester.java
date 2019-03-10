package homework.expression.core;

import java.math.BigInteger;
import java.util.Objects;

public class ComputableTester implements Computable {

    private double value;

    ComputableTester(double value) {
        this.value = value;
    }

    public ComputableTester(long value) {
        this((double) value);
    }

    @Override
    public Computable add(Computable other) {
        assert other instanceof ComputableTester;
        return new ComputableTester(
            value + ((ComputableTester) other).value);
    }

    @Override
    public Computable mul(Computable other) {
        assert other instanceof ComputableTester;
        return new ComputableTester(
            value * ((ComputableTester) other).value);
    }

    @Override
    public Computable pow(Computable index) {
        assert index instanceof ComputableTester;
        double indexValue = ((ComputableTester) index).value;
        return new ComputableTester(Math.pow(value, indexValue));
    }

    @Override
    public Computable constant(BigInteger bigInteger) {
        long longVal = bigInteger.longValue();
        return new ComputableTester(longVal);
    }

    @Override
    public Computable sin() {
        return new ComputableTester(Math.sin(value));
    }

    @Override
    public Computable cos() {
        return new ComputableTester(Math.cos(value));
    }

    @Override
    public Computable variable() {
        return constant(BigInteger.valueOf((long) value));
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
        return ((Double) value).toString();
    }
}
