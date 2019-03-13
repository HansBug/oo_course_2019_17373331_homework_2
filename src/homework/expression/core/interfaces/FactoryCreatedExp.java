package homework.expression.core.interfaces;

public interface FactoryCreatedExp extends Expression {
    ExpFactory getFactory();

    void setFactory(ExpFactory factory);
}
