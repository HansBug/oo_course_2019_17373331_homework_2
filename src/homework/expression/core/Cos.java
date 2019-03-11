package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

public class Cos extends BaseImmutableExp implements Expression {
    private Expression base;

    Cos(Expression base) {
        this.base = base;
    }

    public Expression getBase() {
        return base;
    }

    @Override
    public Expression diff() {
        // cos'(x) = -sin(x)x'
        return base.sin().negate().mul(base.diff());
    }

    @Override
    public Expression negate() {
        return mul(constant(-1));
    }

    @Override
    public Computable compute(Computable docker) {
        return base.compute(docker).cos();
    }

    @Override
    public boolean uncachedIsZero() {
        return false;
    }

    @Override
    public boolean uncachedIsOne() {
        // cos(0) = 1
        return base.isZero();
    }

    @Override
    public String toString() {
        return "cos(" + base.toString() + ")";
    }
}
