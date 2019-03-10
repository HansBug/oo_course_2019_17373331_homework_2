package homework.expression.core;

import java.math.BigInteger;

public interface Computable {
    Computable add(Computable other);

    Computable mul(Computable other);

    Computable pow(Computable index);

    Computable constant(BigInteger bigInteger);

    Computable sin();

    Computable cos();

    Computable variable();
}
