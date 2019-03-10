package homework.expression.core;

import java.math.BigInteger;

public class Pow extends BaseImmutableExp implements Expression {

    private Expression base;
    private Expression index;

    Pow(Expression base, long index) {
        this(base, BigInteger.valueOf(index));
    }

    Pow(Expression base, BigInteger index) {
        this(base, ExpFactory.constant(index));
    }

    Pow(Expression base, Expression index) {
        this.base = base;
        this.index = index;
    }

    public Expression getBase() {
        return base;
    }

    public Expression getIndex() {
        return index;
    }

    @Override
    public boolean checkIsZero() {
        return base.isZero();
    }

    @Override
    public boolean checkIsOne() {
        return indexIsZero();
    }

    private boolean indexIsZero() {
        return index.isZero();
    }

    private boolean indexIsOne() {
        return index.isOne();
    }

    @Override
    public Expression diff() {
        if (index.isZero()) {
            // constant
            return ExpFactory.constant(0);
        } else {
            // (x^a)' = a * x^(a-1)
            assert index instanceof Constant;
            return
                ExpFactory.mul(
                    ExpFactory.mul(index,
                        ExpFactory.pow(base,
                            ExpFactory.add(index, ExpFactory.constant(-1))
                        )
                    ), base.diff()
                );
        }
    }

    @Override
    public Expression negate() {
        return ExpFactory.mul(ExpFactory.constant(-1), this);
    }

    @Override
    public Computable compute(Computable docker) {
        return base.compute(docker).pow(index.compute(docker));
    }

    @Override
    public String toString() {
        return base.toString() + "^" + index.toString();
    }
}
