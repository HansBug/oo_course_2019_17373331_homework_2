package homework.expression.core;

public class VariableHolder implements Expression {

    private Expression hold;

    VariableHolder(Expression hold) {
        this.hold = hold;
    }

    VariableHolder() {
        this(null);
    }

    void fill(Expression hold) throws Exception {
        if (isEmpty()) {
            throw new Exception("filled");
        } else {
            this.hold = hold;
        }
    }

    boolean isEmpty() {
        return hold == null;
    }

    @Override
    public Expression diff() {
        if (isEmpty()) {
            return ExpFactory.constant(1);
        } else {
            return hold.diff();
        }
    }

    @Override
    public Expression negate() {
        return ExpFactory.mul(ExpFactory.constant(-1), this);
    }

    @Override
    public boolean isZero() {
        return !isEmpty() && hold.isZero();
    }

    @Override
    public boolean isOne() {
        return !isEmpty() && hold.isOne();
    }

    @Override
    public Computable compute(Computable docker) {
        return docker.variable();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "x";
        } else {
            return hold.toString();
        }
    }
}
