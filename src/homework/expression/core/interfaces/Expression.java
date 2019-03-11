package homework.expression.core.interfaces;

import java.math.BigInteger;

/**
 * An Expression is a tree node, which can compute a Computable
 * <p>
 * Expression is also computable, when you compute a expression, you are in fact
 * deep cloning it.
 */
public interface Expression extends Computable {
    Expression diff();

    Expression negate();

    boolean isZero();

    boolean isOne();

    Computable compute(Computable docker);

    @Override
    Expression add(Computable other);

    default Expression add(BigInteger other) {
        return add(constant(other));
    }

    default Expression add(long other) {
        return add(constant(other));
    }

    @Override
    Expression mul(Computable other);

    default Expression mul(BigInteger other) {
        return mul(constant(other));
    }

    default Expression mul(long other) {
        return mul(constant(other));
    }

    @Override
    Expression pow(Computable index);

    default Expression pow(BigInteger index) {
        return pow(constant(index));
    }

    default Expression pow(long index) {
        return pow(constant(index));
    }

    @Override
    Expression constant(BigInteger bigInteger);

    @Override
    default Expression constant(long value) {
        return constant(BigInteger.valueOf(value));
    }

    @Override
    Expression sin();

    @Override
    Expression cos();

    @Override
    Expression variable();
}
