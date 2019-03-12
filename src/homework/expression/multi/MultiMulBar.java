package homework.expression.multi;

import homework.expression.core.base.BaseExp;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.Expression;

import java.util.Iterator;
import java.util.LinkedList;

public class MultiMulBar extends BaseExp implements Iterable<Expression> {
    private boolean hasZeroInExps;
    private LinkedList<Expression> exps;

    MultiMulBar() {
        exps = new LinkedList<>();
        hasZeroInExps = false;
    }

    MultiMulBar(MultiMulBar origin) {
        exps = new LinkedList<>(origin.exps);
        hasZeroInExps = false;
    }

    void append(Expression exp) {
        if (isZero()) {
            return;
        }
        if (exp.isZero()) {
            hasZeroInExps = true;
        } else if (exp.isOne()) {
            return;
        }
        exps.add(exp);
    }

    @Override
    public Expression diff() {
        Expression res = new MultiMulBar();
        for (Expression exp :
            exps) {
            res = res.add(new MultiMulBar(this)
                .mul(exp.diff())
                .mul(exp.pow(-1)));
        }
        return res;
    }

    @Override
    public Expression negate() {
        exps.get(0).negate();
        return this;
    }

    @Override
    public boolean isZero() {
        return hasZeroInExps;
    }

    @Override
    public boolean isOne() {
        return exps.isEmpty();
    }

    @Override
    public Computable compute(Computable docker) {
        Computable res = docker.constant(1);
        for (Expression exp :
            exps) {
            res.mul(exp.compute(docker));
        }
        return res;
    }

    @Override
    public Iterator<Expression> iterator() {
        return exps.iterator();
    }
}
