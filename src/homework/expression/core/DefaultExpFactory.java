package homework.expression.core;

import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;
import homework.expression.core.interfaces.ValueExp;

import java.math.BigInteger;

/**
 * Factory of DefaultExpFactory Expression
 * <p>
 * whose constant is a ValueExp
 * and pow can just be computed with a ValueExp
 * <p>
 * To get a x, use var()
 * <p>
 * To get a constant, use constant()
 */
public class DefaultExpFactory implements ExpFactory {

    private DefaultExpFactory() {
    }

    public static DefaultExpFactory getInstance() {
        return FactorySingleton.singleton;
    }

    public Expression mul(Expression exp1, Expression exp2) {
        if (exp1.isZero() || exp2.isZero()) {
            return constant(0);
        }

        if (exp1.isOne()) {
            return exp2;
        } else if (exp2.isOne()) {
            return exp1;
        }

        // const * const
        if (exp1 instanceof ValueExp && exp2 instanceof ValueExp) {
            return constant(((ValueExp) exp1).getValue().multiply(
                ((ValueExp) exp2).getValue()
            ));
        }

        if (exp1 instanceof Sum) {
            return add(
                mul(((Sum) exp1).getExp1(), exp2),
                mul(((Sum) exp1).getExp2(), exp2)
            );
        } else if (exp2 instanceof Sum) {
            return mul(exp2, exp1);
        }

        if (exp1 instanceof Product && exp2 instanceof Product) {
            return new Product(exp1, exp2);
        }

        if (exp1 instanceof Product) {
            return new Product(
                mul(((Product) exp1).getExp1(), exp2),
                ((Product) exp1).getExp2()
            );
        } else if (exp2 instanceof Product) {
            return mul(exp2, exp1);
        }

        return new Product(exp1, exp2);
    }

    public Expression mul(long value, Expression exp2) {
        return mul(constant(value), exp2);
    }

    public Expression mul(BigInteger value, Expression exp2) {
        return mul(constant(value), exp2);
    }

    public Expression add(Expression exp1, Expression exp2) {
        if (exp1.isZero()) {
            return exp2;
        } else if (exp2.isZero()) {
            return exp1;
        }
        if (exp1 instanceof Constant && exp2 instanceof Constant) {
            return new Constant(((Constant) exp1).getValue()
                .add(((Constant) exp2).getValue()));
        }
        return new Sum(exp1, exp2);
    }

    public Expression sub(Expression exp1, Expression exp2) {
        return add(exp1, exp2.negate());
    }

    @Override
    public ValueExp constant(BigInteger value) {
        return new Constant(value);
    }

    public ValueExp constant(long value) {
        return new Constant(value);
    }

    public Expression cos(Expression exp) {
        if (exp.isZero()) {
            return constant(1);
        } else {
            return new Cos(exp);
        }
    }

    public Expression sin(Expression exp) {
        if (exp.isZero()) {
            return constant(0);
        } else {
            return new Sin(exp);
        }
    }

    public Expression pow(Expression exp, BigInteger index) {
        return pow(exp, constant(index));
    }

    public Expression pow(Expression exp, ValueExp index) {
        if (index.isOne()) {
            return exp;
        } else if (index.isZero()) {
            return constant(1);
        }
        if (exp instanceof Pow) {
            Pow powExp = (Pow) exp;
            return pow(powExp.getBase(),
                powExp.getIndex().getValue().multiply(index.getValue()));
        }
        return new Pow(exp, index);
    }

    public Expression var() {
        return new VariableHolder();
    }

    public Expression diff(Expression exp) {
        return exp.diff();
    }

    private static class FactorySingleton {
        private static final DefaultExpFactory singleton =
            new DefaultExpFactory();
    }
}
