package homework.expression.core;

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
        return ExpFactory.add(
            ExpFactory.mul(exp1, exp2.diff()),
            ExpFactory.mul(exp1.diff(), exp2)
        );
    }

    @Override
    public Expression negate() {
        return ExpFactory.mul(exp1.negate(), exp2);
    }

    @Override
    public Computable compute(Computable docker) {
        return exp1.compute(docker).mul(exp2.compute(docker));
    }

    @Override
    public boolean checkIsZero() {
        return false;
    }

    @Override
    public boolean checkIsOne() {
        return exp1.isOne() && exp2.isOne();
    }

    @Override
    public String toString() {
        return exp1.toString() + "*" + exp2.toString();
    }
}
