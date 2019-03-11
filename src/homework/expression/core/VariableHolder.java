package homework.expression.core;

import homework.expression.core.base.BaseExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

import java.util.Objects;

public class VariableHolder extends BaseExp implements Expression {

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
            return constant(1);
        } else {
            return hold.diff();
        }
    }

    @Override
    public Expression negate() {
        return mul(constant(-1));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (hold == null) {
            return false;
        }
        VariableHolder that = (VariableHolder) o;
        return Objects.equals(hold, that.hold);
    }
}
