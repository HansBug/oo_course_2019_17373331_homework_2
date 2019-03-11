package homework.expression.core.interfaces;

import java.math.BigInteger;

public interface ExpFactory {
    Expression add(Expression exp1, Expression exp2);

    Expression mul(Expression exp1, Expression exp2);

    Expression pow(Expression exp1, ValueExp exp2);

    Expression sin(Expression exp);

    Expression cos(Expression exp);

    Expression var();

    Expression constant(BigInteger value);

    default Expression constant(long value) {
        return constant(BigInteger.valueOf(value));
    }
}
