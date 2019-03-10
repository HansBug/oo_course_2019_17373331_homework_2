package homework.expression.core;

public abstract class BaseImmutableExp implements Expression {
    private Boolean isOne;
    private Boolean isZero;

    public abstract boolean checkIsOne();

    public abstract boolean checkIsZero();

    @Override
    public boolean isZero() {
        if (isZero == null) {
            isZero = checkIsZero();
        }
        return isZero;
    }

    @Override
    public boolean isOne() {
        if (isOne == null) {
            isOne = checkIsOne();
        }
        return isOne;
    }
}
