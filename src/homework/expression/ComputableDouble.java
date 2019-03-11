package homework.expression;

import homework.expression.core.interfaces.Computable;

import java.math.BigInteger;
import java.util.Objects;

public class ComputableDouble implements Computable {

    private double value;

    ComputableDouble(double value) {
        this.value = value;
    }

    public ComputableDouble(long value) {
        this((double) value);
    }

    @Override
    public Computable add(Computable other) {
        assert other instanceof ComputableDouble;
        return new ComputableDouble(
            value + ((ComputableDouble) other).value);
    }

    @Override
    public Computable mul(Computable other) {
        assert other instanceof ComputableDouble;
        return new ComputableDouble(
            value * ((ComputableDouble) other).value);
    }

    @Override
    public Computable pow(Computable index) {
        assert index instanceof ComputableDouble;
        double indexValue = ((ComputableDouble) index).value;
        return new ComputableDouble(Math.pow(value, indexValue));
    }

    @Override
    public Computable constant(BigInteger bigInteger) {
        long longVal = bigInteger.longValue();
        return new ComputableDouble(longVal);
    }

    @Override
    public Computable sin() {
        return new ComputableDouble(Math.sin(value));
    }

    @Override
    public Computable cos() {
        return new ComputableDouble(Math.cos(value));
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
        ComputableDouble that = (ComputableDouble) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public String toString() {
        return ((Double) value).toString();
    }
}
