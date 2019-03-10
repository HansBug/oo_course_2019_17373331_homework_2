package homework.expression.core;

public interface Expression {
    Expression diff();

    Expression negate();

    boolean isZero();

    boolean isOne();

    Computable compute(Computable docker);
}
