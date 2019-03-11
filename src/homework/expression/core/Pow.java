package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;
import homework.expression.core.interfaces.ValueExp;

public class Pow extends BaseImmutableExp implements Expression {

    private Expression base;
    private ValueExp index;

    Pow(Expression base, ValueExp index) {
        this.base = base;
        this.index = index;
    }

    public Expression getBase() {
        return base;
    }

    public ValueExp getIndex() {
        return index;
    }

    @Override
    public boolean uncachedIsZero() {
        return base.isZero();
    }

    @Override
    public boolean uncachedIsOne() {
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
            return constant(0);
        } else {
            // (x^a)' = a * x^(a-1)
            return base.diff().mul(index).mul(
                base.pow(index.add(constant(-1)))
            );
        }
    }

    @Override
    public Expression negate() {
        return mul(constant(-1));
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
