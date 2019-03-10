package homework.expression.core;

public class Sin extends BaseImmutableExp implements Expression {
    private Expression base;

    Sin(Expression base) {
        this.base = base;
    }

    @Override
    public Expression diff() {
        return ExpFactory.mul(ExpFactory.cos(base), base.diff());
    }

    @Override
    public Expression negate() {
        return ExpFactory.mul(-1, this);
    }

    @Override
    public Computable compute(Computable docker) {
        return base.compute(docker).sin();
    }

    @Override
    public boolean checkIsZero() {
        return base.isZero();
    }

    @Override
    public boolean checkIsOne() {
        return false;
    }

    @Override
    public String toString() {
        return "sin(" + base.toString() + ")";
    }
}
