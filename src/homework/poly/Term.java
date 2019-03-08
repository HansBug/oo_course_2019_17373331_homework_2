package homework.poly;

public interface Term {
    Term add(Term other);

    Term sub(Term other);

    Term negate();

    Term getDerivative();
}
