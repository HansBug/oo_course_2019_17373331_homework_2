package homework.expression.multi;

import homework.expression.core.DefaultExpFactory;
import homework.expression.core.Product;
import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;
import homework.expression.core.interfaces.ValueExp;

import java.math.BigInteger;

public class MultiExpFactory implements ExpFactory {
    private static final ExpFactory defaultFactory =
        DefaultExpFactory.getInstance();

    public static MultiExpFactory getInstance() {
        return Singleton.singleton;
    }

    @Override
    public Expression add(Expression exp1, Expression exp2) {
        return defaultFactory.add(exp1, exp2);
    }

    @Override
    public Expression mul(Expression exp1, Expression exp2) {
        // exp1 is the caller
        if (exp1 instanceof Product) {
            MultiMulBar mulBar = new MultiMulBar();
            mulBar.append(((Product) exp1).getExp1());
            mulBar.append(((Product) exp1).getExp2());
            if (exp2 instanceof Product) {
                mulBar.append(((Product) exp2).getExp1());
                mulBar.append(((Product) exp2).getExp2());
            } else {
                mulBar.append(exp2);
            }
            return mulBar;
        }
        if (exp2 instanceof Product) {
            return mul(exp2, exp1);
        }
        if (exp1 instanceof MultiMulBar) {
            if (exp2 instanceof MultiMulBar) {
                for (Expression exp :
                    (MultiMulBar) exp2) {
                    ((MultiMulBar) exp1).append(exp);
                }
            } else {
                ((MultiMulBar) exp1).append(exp2);
            }
            return exp1;
        }
        return defaultFactory.mul(exp1, exp2);
    }

    @Override
    public Expression pow(Expression exp1, ValueExp exp2) {
        return defaultFactory.pow(exp1, exp2);
    }

    @Override
    public Expression sin(Expression exp) {
        return defaultFactory.sin(exp);
    }

    @Override
    public Expression cos(Expression exp) {
        return defaultFactory.cos(exp);
    }

    @Override
    public Expression var() {
        return defaultFactory.var();
    }

    @Override
    public Expression constant(BigInteger value) {
        return defaultFactory.constant(value);
    }

    private static class Singleton {
        private static final MultiExpFactory singleton = new MultiExpFactory();
    }
}
