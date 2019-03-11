package homework.expression.core;

import homework.expression.core.base.BaseImmutableExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

/**
 * A Product is a Expression seems like exp1 * exp2...
 */
public class Product extends BaseImmutableExp implements Expression {

    private Expression exp1;
    private Expression exp2;

    Product(Expression exp1, Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public Expression getExp1() {
        return exp1;
    }

    public Expression getExp2() {
        return exp2;
    }

    @Override
    public Expression diff() {
        return exp2.diff().mul(exp1).add(exp1.diff().mul(exp2));
    }

    @Override
    public Expression negate() {
        return exp1.negate().mul(exp2);
    }

    @Override
    public Computable compute(Computable docker) {
        return exp1.compute(docker).mul(exp2.compute(docker));
    }

    @Override
    public boolean uncachedIsZero() {
        return false;
    }

    @Override
    public boolean uncachedIsOne() {
        return exp1.isOne() && exp2.isOne();
    }

    @Override
    public String toString() {
        return exp1.toString() + "*" + exp2.toString();
    }
}
