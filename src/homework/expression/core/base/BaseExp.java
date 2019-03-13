package homework.expression.core.base;

import homework.expression.core.DefaultExpFactory;
import homework.expression.core.interfaces.Computable;
import homework.expression.core.interfaces.ExpFactory;
import homework.expression.core.interfaces.Expression;
import homework.expression.core.interfaces.FactoryCreatedExp;
import homework.expression.core.interfaces.ValueExp;

import java.math.BigInteger;

public abstract class BaseExp implements Expression, FactoryCreatedExp {

    private ExpFactory factory = DefaultExpFactory.getInstance();

    @Override
    public ExpFactory getFactory() {
        return factory;
    }

    @Override
    public void setFactory(ExpFactory factory) {
        this.factory = factory;
    }

    @Override
    public Expression add(Computable other) {
        assert other instanceof Expression;
        return getFactory().add(this, (Expression) other);
    }

    @Override
    public Expression mul(Computable other) {
        assert other instanceof Expression;
        return getFactory().mul(this, (Expression) other);
    }

    @Override
    public Expression pow(Computable other) {
        assert other instanceof ValueExp;
        return getFactory().pow(this, (ValueExp) other);
    }

    @Override
    public Expression constant(BigInteger bigInteger) {
        return getFactory().constant(bigInteger);
    }

    @Override
    public Expression sin() {
        return getFactory().sin(this);
    }

    @Override
    public Expression cos() {
        return getFactory().cos(this);
    }

    @Override
    public Expression variable() {
        return getFactory().var();
    }
}
