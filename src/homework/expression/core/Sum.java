package homework.expression.core;

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
        return ExpFactory.add(exp1.diff(), exp2.diff());
    }

    @Override
    public Expression negate() {
        return ExpFactory.add(exp1.negate(), exp2.negate());
    }

    @Override
    public Computable compute(Computable docker) {
        return exp1.compute(docker).add(exp2.compute(docker));
    }

    @Override
    public boolean checkIsZero() {
        return exp1.isZero() && exp2.isZero();
    }

    @Override
    public boolean checkIsOne() {
        return (exp1.isOne() && exp2.isZero()) ||
            (exp1.isZero() && exp2.isOne());
    }

    @Override
    public String toString() {
        return exp1.toString() + "+" + exp2.toString();
    }
}
