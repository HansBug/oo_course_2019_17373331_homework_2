package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;
import homework.expression.core.interfaces.ValueExp;

import java.math.BigInteger;
import java.util.Objects;

class Constant extends BaseImmutableExp implements ValueExp {

    private BigInteger value;

    Constant(BigInteger value) {
        this.value = value;
    }

    Constant(long value) {
        this(BigInteger.valueOf(value));
    }

    Constant() {
        this(0);
    }

    public BigInteger getValue() {
        return value;
    }

    public Expression diff() {
        return constant(0);
    }

    public Expression negate() {
        return constant(value.negate());
    }

    @Override
    public Computable compute(Computable docker) {
        return docker.constant(value);
    }

    @Override
    public boolean uncachedIsOne() {
        return BigInteger.ONE.equals(value);
    }

    @Override
    public boolean uncachedIsZero() {
        return BigInteger.ZERO.equals(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public int compareTo(BigInteger value) {
        return this.value.compareTo(value);
    }

    public int compareTo(Constant value) {
        return this.value.compareTo(value.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Constant constant = (Constant) o;
        return Objects.equals(value, constant.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
