package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

public class Sum extends BaseImmutableExp implements Expression {

    private Expression exp1;
    private Expression exp2;

    Sum(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    Expression getExp1() {
        return exp1;
    }

    Expression getExp2() {
        return exp2;
    }

    @Override
    public Expression diff() {
        return exp1.diff().add(exp2.diff());
    }

    @Override
    public Expression negate() {
        return exp1.negate().add(exp2.negate());
    }

    @Override
    public Computable compute(Computable docker) {
        return exp1.compute(docker).add(exp2.compute(docker));
    }

    @Override
    public boolean uncachedIsZero() {
        return exp1.isZero() && exp2.isZero();
    }

    @Override
    public boolean uncachedIsOne() {
        return (exp1.isOne() && exp2.isZero()) ||
            (exp1.isZero() && exp2.isOne());
    }

    @Override
    public String toString() {
        return exp1.toString() + "+" + exp2.toString();
    }
}
