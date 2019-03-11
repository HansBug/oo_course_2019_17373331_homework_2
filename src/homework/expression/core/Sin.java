package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

public class Sin extends BaseImmutableExp implements Expression {
    private Expression base;

    Sin(Expression base) {
        this.base = base;
    }

    @Override
    public Expression diff() {
        return base.cos().mul(base.diff());
    }

    @Override
    public Expression negate() {
        return mul(constant(-1));
    }

    @Override
    public Computable compute(Computable docker) {
        return base.compute(docker).sin();
    }

    @Override
    public boolean uncachedIsZero() {
        return base.isZero();
    }

    @Override
    public boolean uncachedIsOne() {
        return false;
    }

    @Override
    public String toString() {
        return "sin(" + base.toString() + ")";
    }
}
