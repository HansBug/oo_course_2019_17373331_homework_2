package homework.expression.core;

import java.math.BigInteger;

class Constant extends BaseImmutableExp implements Expression {

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
        return new Constant(0);
    }

    public Expression negate() {
        return new Constant(value.negate());
    }

    @Override
    public Computable compute(Computable docker) {
        return docker.constant(value);
    }

    @Override
    public boolean checkIsOne() {
        return BigInteger.ONE.equals(value);
    }

    @Override
    public boolean checkIsZero() {
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
}