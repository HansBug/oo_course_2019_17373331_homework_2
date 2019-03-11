package homework.expression.core.interfaces;

import java.math.BigInteger;

public interface Computable {
    Computable add(Computable other);

    Computable mul(Computable other);

    Computable pow(Computable index);

    Computable constant(BigInteger bigInteger);

    default Computable constant(long value) {
        return constant(BigInteger.valueOf(value));
    }

    Computable sin();

    Computable cos();

    Computable variable();
}
