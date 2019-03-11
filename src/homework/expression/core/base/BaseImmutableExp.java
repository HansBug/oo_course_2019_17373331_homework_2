package homework.expression.core.base;

public abstract class BaseImmutableExp extends BaseExp {
    private Boolean cachedIsOne;
    private Boolean cachedIsZero;

    public abstract boolean uncachedIsOne();

    public abstract boolean uncachedIsZero();

    @Override
    public boolean isZero() {
        if (cachedIsZero == null) {
            cachedIsZero = uncachedIsZero();
        }
        return cachedIsZero;
    }

    @Override
    public boolean isOne() {
        if (cachedIsOne == null) {
            cachedIsOne = uncachedIsOne();
        }
        return cachedIsOne;
    }
}
