package homework.expression.core;

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
        return ExpFactory.mul(ExpFactory.sin(base).negate(), base.diff());
    }

    @Override
    public Expression negate() {
        return ExpFactory.mul(-1, this);
    }

    @Override
    public Computable compute(Computable docker) {
        return base.compute(docker).cos();
    }

    @Override
    public boolean checkIsZero() {
        return false;
    }

    @Override
    public boolean checkIsOne() {
        // cos(0) = 1
        return base.isZero();
    }

    @Override
    public String toString() {
        return "cos(" + base.toString() + ")";
    }
}
