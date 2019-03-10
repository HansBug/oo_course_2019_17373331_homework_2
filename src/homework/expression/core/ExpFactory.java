package homework.expression.core;

import java.math.BigInteger;

/**
 * Factory of ExpFactory Expression
 * <p>
 * To get a x, use var()
 * <p>
 * To get a constant, use constant()
 */
public class ExpFactory {
    public static Expression mul(Expression exp1, Expression exp2) {
        if (exp1.isZero() || exp2.isZero()) {
            return constant(0);
        }

        if (exp1.isOne()) {
            return exp2;
        } else if (exp2.isOne()) {
            return exp1;
        }

        // const * const
        if (exp1 instanceof Constant && exp2 instanceof Constant) {

            return constant(((Constant) exp1).getValue().multiply(
                ((Constant) exp2).getValue()
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

    public static Expression mul(long value, Expression exp2) {
        return mul(constant(value), exp2);
    }

    public static Expression mul(BigInteger value, Expression exp2) {
        return mul(constant(value), exp2);
    }

    public static Expression add(Expression exp1, Expression exp2) {
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

    public static Expression sub(Expression exp1, Expression exp2) {
        return add(exp1, exp2.negate());
    }

    public static Expression constant(BigInteger value) {
        return new Constant(value);
    }

    public static Expression constant(long value) {
        return new Constant(value);
    }

    public static Expression cos(Expression exp) {
        if (exp.isZero()) {
            return constant(1);
        } else {
            return new Cos(exp);
        }
    }

    public static Expression sin(Expression exp) {
        if (exp.isZero()) {
            return constant(0);
        } else {
            return new Sin(exp);
        }
    }

    public static Expression pow(Expression exp, Expression index) {
        assert index instanceof Constant;
        if (index.isOne()) {
            return exp;
        } else if (index.isZero()) {
            return constant(1);
        }
        if (exp instanceof Pow) {
            return new Pow(((Pow) exp).getBase(), mul(((Pow) exp).getIndex(),
                index));
        }
        return new Pow(exp, index);
    }

    public static Expression pow(Expression exp, BigInteger index) {
        return pow(exp, constant(index));
    }

    public static Expression var() {
        return new VariableHolder();
    }

    public static Expression diff(Expression exp) {
        return exp.diff();
    }
}
